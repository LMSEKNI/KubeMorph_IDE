import { Component, OnInit } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import { CharttemplateComponent} from '../charttemplate/charttemplate.component';

@Component({
  selector: 'app-helm-details',
  templateUrl: './helm-details.component.html',
  styleUrls: ['./helm-details.component.scss']
})
export class HelmDetailsComponent implements OnInit {

  constructor(public dialog: MatDialog) {}

  ngOnInit(): void {
  }
  openDialog(): void {
    const dialogRef = this.dialog.open(CharttemplateComponent, {
      width: '800px', // Adjust width as needed
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
}
