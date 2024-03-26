import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'getName'
})
export class GetNamePipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): unknown {


    value=value.charAt(0).toUpperCase() + value.slice(1);
    return value.split("@")[0];
  }

}
