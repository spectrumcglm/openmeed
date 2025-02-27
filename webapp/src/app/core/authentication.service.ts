import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private router: Router) {

  }

  public isLoggedIn() {
    return window.sessionStorage.getItem('access_token') !== null;
  }

  public logout() {
    window.sessionStorage.removeItem('access_token');
  }

}
