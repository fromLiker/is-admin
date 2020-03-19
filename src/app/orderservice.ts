import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable} from 'rxjs';
import { Order } from './order';

@Injectable({
  providedIn: 'root'
})
export class OrderServiceService {
  readonly Url = 'api/order/orders';

  constructor(
    private http: HttpClient
  ) { }


  searchorder(order: Order): Observable<any> {
    console.log('searchorder() done!');
    return this.http.post<any>(this.Url, order);
  }

}
