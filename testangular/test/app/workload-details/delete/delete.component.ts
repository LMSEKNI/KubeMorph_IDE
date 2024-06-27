import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent implements OnInit {
  @Input() resourceName: string | null = null;

  constructor() { }

  ngOnInit(): void {
  }

}
