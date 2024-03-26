import { Component } from '@angular/core';
import { animate, keyframes, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss'],
  animations: [
    trigger('rotate', [
      transition(':enter', [
        animate('600ms', 
          keyframes([
            style({transform: 'rotate(0deg)', offset: '0'}),
            style({transform: 'rotate(180deg)', offset: '1'})
          ])
        )
      ])
    ]),
  ]
})
export class ErrorComponent {

}
