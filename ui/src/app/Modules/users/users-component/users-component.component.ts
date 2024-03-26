import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { AddUserComponent } from '../add-user/add-user.component';
import { DeleteUserComponent } from '../delete-user/delete-user.component';
import { EditUserComponent } from '../edit-user/edit-user.component';
import { UsersService } from '../services/users.service';
import { Subscription } from 'rxjs';
import { ErrorComponent } from '../../shared/components/error/error.component';
import { HttpErrorResponse } from '@angular/common/http';




export type UserDataInterface = {
  id: number;
  name: string;
  email: string;
  role: string;
}

@Component({
  selector: 'app-users-component',
  templateUrl: './users-component.component.html',
  styleUrls: ['./users-component.component.scss'],
  providers: [UsersService]
})


export class UsersComponentComponent {

  isSelected:boolean = false;
  displayedColumns: string[] = ['id', 'empName', 'empEmail', 'empRole', 'edit', 'delete'];
  hName: string[] = ['ID', 'Name', 'Email', 'Role', 'Edit', 'Delete'];
  dataSource!: MatTableDataSource<UserDataInterface>;
  searchText: string="";
  user_list: any;
  userDataSub: Subscription | undefined;
  adminCode:string = sessionStorage.getItem('adminCode')!;
  public columnsOfUser: string[] = [
    'userId',
    'email',
    'email',
    'priviledgeName',
  ];


  public isLoading:boolean = true;





  @ViewChild(MatSort) sort!: MatSort;

  constructor(private router: Router, private dialog: MatDialog, private usersDataService: UsersService) {



  }

  ngOnInit(): void {

    this.loadData();


  }

  // Loading all users
  loadData() {

    this.userDataSub = this.usersDataService
      .getAllUsers()
      .subscribe({
        next: (usersData) => {
          this.user_list = usersData;
          this.user_list = this.user_list.filter((val: any) => {
            return val.active == true && val.empCode !=this.adminCode
          })
          this.isLoading = false;
          this.dataSource = new MatTableDataSource(this.user_list);
          this.dataSource.sort = this.sort;
        },
        error: (error:HttpErrorResponse) => { if(error.status!=401){
          this.router.navigate(['something-went-wrong']);
        } else{
          this.dialog.open(ErrorComponent, {
              panelClass: 'modal',
              
              width: '500px',
              backdropClass: 'backdropBackground',
              disableClose: true,
          });
      }},
        complete: () => { console.log("All users fetched.")}
      }
      );
  }

  ngOnDestroy(): void {
    this.userDataSub?.unsubscribe();
  }

  stopLoading() {
    this.isLoading = false;
  }
 




// Search filter
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

// Add user Dialog
  openDialog(title: string): void {
    const dialogRef = this.dialog.open(AddUserComponent, { panelClass: 'modal', data: { title }, width: '800px', maxHeight: '95vh', backdropClass: 'backdropBackground', disableClose: true });


    dialogRef.afterClosed().subscribe(
      {
        next:(res) => {
          if (res.reload) {
            this.loadData();
            this.isLoading=true;
            }
          },

        error(err) {
          console.log(err);
          
        },
        complete:()=>{
          console.log("Done.");
          
        }
      }
    );
  }

// Delete User Dialog
  openDeleteUserDialog(userData: string): void {

    const dialogRef = this.dialog.open(DeleteUserComponent, { panelClass: 'modal', width: '600px', backdropClass: 'backdropBackground', maxHeight: '95vh', data: { userToBeDeleted: userData }, disableClose: true });

    dialogRef.afterClosed().subscribe(
      {
        next:(res) => {
          if (res.reload) {
            this.loadData();
            this.isLoading=true;
            }
          },

        error(err) {
          console.log(err);
          
        },
        complete:()=>{
          console.log("Delete Done.");
          
        }
      }
    );
  }


  // Edit user dialog
  openEditUserDialog(userData: string): void {
    console.log(userData);

    const dialogRef = this.dialog.open(EditUserComponent, { panelClass: 'modal', width: '800px', backdropClass: 'backdropBackground', maxHeight: '95vh', data: { data: userData }, disableClose: true });


    dialogRef.afterClosed().subscribe(
      {
        next:(res) => {
          if (res.reload) {
            this.loadData();
            this.isLoading=true;
            }
          },

        error(err) {
          console.log(err);
          
        },
        complete:()=>{
          console.log("Done.");
          
        }
      }
    );
  }

  // Toggle view
  tableView() {
    this.isSelected = !this.isSelected;
  }


}
