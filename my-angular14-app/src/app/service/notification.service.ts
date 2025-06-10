import { Injectable } from '@angular/core';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {BehaviorSubject} from "rxjs";
import {Notification} from "../model/notification.model";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private stompClient: Client;

  private notifications$ = new BehaviorSubject<Notification | null>(null);

  constructor() {
    this.stompClient = new Client({
      brokerURL: undefined, // use SockJS
      webSocketFactory: () => new SockJS('http://localhost:8083/ws'), // Spring에서 `/ws`로 매핑된 엔드포인트
      reconnectDelay: 5000
    });

    this.stompClient.onConnect = frame => {
      console.log('Connected: ' + frame);
      this.stompClient.subscribe('/topic/notifications', (message: IMessage) => {
        const notif: Notification = JSON.parse(message.body);
        this.notifications$.next(notif);
        console.log('🔔 NotificationModel:', notif);
        // 여기서 컴포넌트에 전달하거나 store에 저장
      });
    };

    this.stompClient.activate();
  }

  getNotificationStream() {
    return this.notifications$.asObservable();
  }


  sendTransferMessage(event: { amount: number }) {
    this.stompClient.publish({
      destination: '/app/transfer',
      body: JSON.stringify(event)
    });
  }
}
