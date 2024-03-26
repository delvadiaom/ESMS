import { Pipe, PipeTransform } from '@angular/core';
import { retry } from 'rxjs';

@Pipe({
    name: 'currencyConverter',
})

export class CurrencyConverterPipe implements PipeTransform {
    transform(value: any): any {
      if (value >= 1000000000) {
        return (value / 100000000).toFixed(2) + ' B';
      } else if (value >= 1000000) {
        return (value / 100000).toFixed(2) + ' M';
      } else if (value >= 1000) {
        return (value / 1000).toFixed(2) + ' K';
      } else {
        return value;
      }
    }
}
