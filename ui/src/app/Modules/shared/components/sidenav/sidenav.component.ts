import { animate, keyframes, style, transition, trigger } from '@angular/animations';
import { Component, Output, EventEmitter, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';

interface SideNavToggle {
    screenWidth: number;
    collapsed: boolean;
}

@Component({
    selector: 'app-sidenav',
    templateUrl: './sidenav.component.html',
    styleUrls: ['./sidenav.component.scss'],
    animations: [
        trigger('fadeInOut', [
            transition(':enter', [
                style({ opacity: 0 }),
                animate('350ms',
                    style({ opacity: 1 })
                )
            ]),
            transition(':leave', [
                style({ opacity: 1 }),
                animate('350ms',
                    style({ opacity: 0 })
                )
            ])
        ]),
        trigger('rotate', [
            transition(':enter', [
                animate('1000ms',
                    keyframes([
                        style({ transform: 'rotate(0deg)', offset: '0' }),
                        style({ transform: 'rotate(2turn)', offset: '1' })
                    ])
                )
            ])
        ])
    ]
})
export class SidenavComponent implements OnInit {

    @Output() onToggleSideNav: EventEmitter<SideNavToggle> = new EventEmitter();
    collapsed = false;
    screenWidth = 0;
    navData = [
        {
            routeLink: 'dashboard',
            icon: 'fa-solid fa-house',
            label: 'Dashboard',
        },
        {
            routeLink: 'employees',
            icon: 'fa-solid fa-briefcase',
            label: 'Employees',
        },
        {
            routeLink: 'activitylogs',
            icon: 'fa-solid fa-chart-line',
            label: 'Activity Logs',
        },
        {
            routeLink: 'users',
            icon: 'fa-solid fa-users',
            label: 'Users',
        },
        {
            routeLink: 'roles',
            icon: 'fa-solid fa-people-roof',
            label: 'User Roles',
        },
        {
            routeLink: 'departments',
            icon: 'fas fa-building',
            label: 'Departments',
        },
        {
            routeLink: 'reports',
            icon: 'fa-solid fa-file',
            label: 'Reports',
        },
    ];

    constructor(private _router:Router){

    }

    @HostListener('window:resize', ['$event'])
    onResize(event: any) {
        this.screenWidth = window.innerWidth;
        if (this.screenWidth <= 768) {
            this.collapsed = false;
            this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
        }
    }

    logout() {
        sessionStorage.removeItem("token");
        this._router.navigate([''])
    }

    ngOnInit(): void {
        this.screenWidth = window.innerWidth;
    }

    toggleCollapse(): void {
        this.collapsed = !this.collapsed;
        this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
    }

    closeSidenav(): void {
        this.collapsed = false;
        this.onToggleSideNav.emit({ collapsed: this.collapsed, screenWidth: this.screenWidth });
    }
}
