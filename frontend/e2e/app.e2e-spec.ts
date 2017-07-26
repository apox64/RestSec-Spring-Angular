import { RestsecfrontendPage } from './app.po';

describe('restsecfrontend App', () => {
  let page: RestsecfrontendPage;

  beforeEach(() => {
    page = new RestsecfrontendPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
