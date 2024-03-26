import { CurrencyConverterPipe } from './currency-converter.pipe';

describe('CurrencyConverterPipe', () => {
  it('create an instance', () => {
    const pipe = new CurrencyConverterPipe();
    expect(pipe).toBeTruthy();
  });

  it('transforms "2000000000" to "2B"', () => {
    const pipe = new CurrencyConverterPipe();
    expect(pipe.transform(2000000000)).toBe("20.00 B");
  });

  it('transforms "100001" to "100.00 K"', () => {
    const pipe = new CurrencyConverterPipe();
    expect(pipe.transform(100001)).toBe("100.00 K");
  });


});
