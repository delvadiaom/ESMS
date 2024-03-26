import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BodyComponent } from './body.component';
import { RouterModule } from '@angular/router';

describe('BodyComponent', () => {
    let component: BodyComponent;
    let fixture: ComponentFixture<BodyComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [BodyComponent],
            imports:[RouterModule]
        })
            .compileComponents();

        fixture = TestBed.createComponent(BodyComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
