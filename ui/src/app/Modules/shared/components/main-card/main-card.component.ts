import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'app-main-card',
    templateUrl: './main-card.component.html',
    styleUrls: ['./main-card.component.scss'],
})
export class MainCardComponent {
    @Input() cardData: any;
    @Input() cardRoute!: any;
}
