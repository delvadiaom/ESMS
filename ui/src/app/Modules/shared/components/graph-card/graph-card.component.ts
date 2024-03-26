import { AfterViewInit, Component, Input } from '@angular/core';
import * as Highcharts from 'highcharts';

@Component({
    selector: 'app-graph-card',
    templateUrl: './graph-card.component.html',
    styleUrls: ['./graph-card.component.scss'],
})
export class GraphCardComponent {
  @Input() chartoptions: any;
  Highcharts: typeof Highcharts = Highcharts; // required
  chartOptions: any;
  constructor() { }

    ngOnChanges() {
        this.chartOptions = this.chartoptions;
    }
}
