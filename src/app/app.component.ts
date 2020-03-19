import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Order } from './order';
import { OrderServiceService } from './orderservice';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'homepage';
  authenticated = false;
  credentials = {username: 'xx', password: '123'};

  orderid: string;
  orders = Order[this.orderid];
  orderform = new Order();

  ngOnInit() {
  }

  constructor(private http: HttpClient, private orderservice: OrderServiceService) {
  // constructor(private http: HttpClient) {
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
    console.log('this.orderform', this.orderform.orderid);
    if (this.orderform.orderid === undefined || this.orderform.orderid === '') {
      alert('Order ID is required!');
      this.orderform.orderid = '';
    } else {
      this.orderservice.searchorder(this.orderform)
      .subscribe(
        res => {
          if (res.status === 200) {
            console.log('response', res);
            this.orders = res.data;
            console.log('this.orderform', this.orders);
            this.orderform.orderid = '';
          } else {
            alert (res.msg);
          }
        },
        error => {
          // this.errMsg = error;
          alert('this is a error:' + error );
        }
      );
    }
  }



  // import { Component } from '@angular/core';
  // import { HttpClient } from '@angular/common/http';

  // @Component({
  //   selector: 'app-root',
  //   templateUrl: './app.component.html',
  //   styleUrls: ['./app.component.scss']
  // })
  // export class AppComponent {
  //   title = 'Likers homepage';
  //   authenticated = false;
  //   credentials = {username: 'xx', password: '123'};
  //   order = {};

  //   constructor(private http: HttpClient) {
  //     this.http.get('me').subscribe(data => {
  //       if (data) {
  //         this.authenticated = true;
  //       }
  //       if (!this.authenticated) {
  //         window.location.href = 'http://auth.ibm.com:9090/oauth/authorize?' +
  //           'client_id=admin&' +
  //           'redirect_uri=http://admin.ibm.com:8080/oauth/callback&' +
  //           'response_type=code&' +
  //           'state=abc';
  //       }
  //     });
  //   }

  //   getOrder() {
  //     this.http.get('api/order/orders/1').subscribe(data => {
  //       this.order = data;
  //     }, () => {
  //       alert('get order fail');
  //     });
  //   }
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
