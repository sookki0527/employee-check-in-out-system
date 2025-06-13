import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = 'http://localhost:8080/api/auth'; // 백엔드 인증 API
  private isAuthenticated$ = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient) {}

  register(data: { username: string; password: string; email: string; isAdmin?: boolean }): Observable<any> {
    return this.http.post('http://localhost:8080/api/auth/register', data, {
      headers: {
        'Content-Type': 'application/json',      // <- 기본값
      },
      withCredentials: true
    });
  }

  login(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post(`${this.authUrl}/login`, credentials).pipe(
      tap((response: any) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('userId', response.id);
        this.isAuthenticated$.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.isAuthenticated$.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): Observable<boolean> {
    return this.isAuthenticated$.asObservable();
  }

  // checkToken(): void {
  //   const token = this.getToken();
  //   this.isAuthenticated$.next(!!token);
  // }
}
