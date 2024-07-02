import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss']
})
export class DeleteComponent implements OnInit {
  @Input() resourceName: string | null = null;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { resourceName: string }) {}


  ngOnInit(): void {
  }

}
