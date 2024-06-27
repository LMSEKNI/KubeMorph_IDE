import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-charttemplate',
  templateUrl: './charttemplate.component.html',
  styleUrls: ['./charttemplate.component.scss']
})
export class CharttemplateComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<CharttemplateComponent>) {}

  ngOnInit(): void {
  }
  closeDialog(): void {
    this.dialogRef.close();
  }
}
