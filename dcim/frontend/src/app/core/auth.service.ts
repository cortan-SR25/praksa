import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http'; import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs'; import { Session } from './models';
const KEY='dcim.session';
@Injectable({providedIn:'root'}) export class AuthService {
  private readonly state=signal<Session|null>(this.read());
  readonly session=this.state.asReadonly(); readonly authenticated=computed(()=>!!this.state()); readonly isAdmin=computed(()=>this.state()?.role==='ADMIN');
  constructor(private http:HttpClient,private router:Router){}
  login(username:string,password:string):Observable<Session>{return this.http.post<Session>('/api/auth/login',{username,password}).pipe(tap(s=>{localStorage.setItem(KEY,JSON.stringify(s));this.state.set(s);}));}
  logout():void{localStorage.removeItem(KEY);this.state.set(null);void this.router.navigateByUrl('/login');}
  token():string|null{return this.state()?.accessToken??null;}
  private read():Session|null{try{const raw=localStorage.getItem(KEY);if(!raw)return null;const s=JSON.parse(raw) as Session;if(new Date(s.expiresAt)<=new Date()){localStorage.removeItem(KEY);return null;}return s;}catch{return null;}}
}
