import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-error',
  templateUrl: './error.component.html',
  styleUrls: ['../../entities/feed/feed.component.scss'],
})
export class ErrorComponent implements OnInit {
  errorMessage?: string;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.data.subscribe(routeData => {
      if (routeData.errorMessage) {
        this.errorMessage = routeData.errorMessage;
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
