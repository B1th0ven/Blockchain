import { StringLimitPipe } from './string-limit.pipe';

describe('StringLimitPipe', () => {
  it('create an instance', () => {
    const pipe = new StringLimitPipe();
    expect(pipe).toBeTruthy();
  });
});
