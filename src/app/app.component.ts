import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Likers homepage';
  authenticated = false;
  credentials = {username: 'xx', password: '123'};
  order = {};

  constructor(private http: HttpClient) {
    this.http.get('me').subscribe(data => {
      if (data) {
        this.authenticated = true;
      }
      if (!this.authenticated) {
        window.location.href = 'http://auth.ibm.com:9090/oauth/authorize?' +
          'client_id=admin&' +
          'redirect_uri=http://admin.ibm.com:8080/oauth/callback&' +
          'response_type=code&' +
          'state=abc';
      }
    });
  }

  getOrder() {
    this.http.get('api/order/orders/1').subscribe(data => {
      this.order = data;
    }, () => {
      alert('get order fail');
    });
  }

  logout() {
    this.http.post('logout', this.credentials).subscribe(() => {
      // this.authenticated = false;
      // defualt : redirect_uri is unknown need overwirte
      window.location.href = 'http://auth.ibm.com:9090/logout?redirect_uri=http://admin.ibm.com:8080';
    }, () => {
      alert('logout fail');
    });
  }

  login() {
    this.http.post('login', this.credentials).subscribe(() => {
      this.authenticated = true;
    }, () => {
      alert('login fail!');
    });
  }
}
