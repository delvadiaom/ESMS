import { GetNamePipe } from './get-name.pipe';

describe('GetNamePipe', () => {
  it('create an instance', () => {
    const pipe = new GetNamePipe();
    expect(pipe).toBeTruthy();
  });
});
