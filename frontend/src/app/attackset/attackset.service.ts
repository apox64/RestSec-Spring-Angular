import {Injectable} from '@angular/core';
import {Http, Headers} from '@angular/http';

@Injectable()
export class AttacksetService {

  constructor (private _http: Http) {}

  getAttackset() {
    return this._http.get('reporting/attackset/')
      .map(res => res.json());
  }

  deleteAttackableEndpoint(id: string) {
    return this._http.delete('reporting/attackset/' + id)
      .map(res => res.json());
  }

}
