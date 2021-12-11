import {
  AfterViewInit,
  Component,
  OnInit,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'jhi-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.scss'],
})
export class SearchComponent implements OnInit, AfterViewInit {
  tabSelectedSearch: number;
  totalTabSearch: number;
  resultSearch: any;
  @ViewChild('modal') modal: TemplateRef<any>;

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.totalTabSearch = 3;
    this.tabSelectedSearch = 0;
  }

  ngAfterViewInit(): void {
    console.warn(this.modal);
    this.openModal();
  }

  openModal(): void {
    const modal = this.dialog.open(this.modal, {
      width: '80%',
      disableClose: true,
      closeOnNavigation: true,
    });

    modal.afterClosed().subscribe(result => {
      console.warn(result);
    });
  }

  selectedNextTabSearch(): void {
    this.tabSelectedSearch = (this.tabSelectedSearch + 1) % this.totalTabSearch;
  }
  selectedPreviesTabSearch(): void {
    this.tabSelectedSearch = (this.tabSelectedSearch - 1) % this.totalTabSearch;
  }
}
