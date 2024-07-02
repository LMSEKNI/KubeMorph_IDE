import {Component, Input, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import { CharttemplateComponent} from '../charttemplate/charttemplate.component';
import {ActivatedRoute, Router} from '@angular/router';
import { Charttemplate2Component} from '../charttemplate2/charttemplate2.component';
import {Location} from '@angular/common';

@Component({
  selector: 'app-helm-details',
  templateUrl: './helm-details.component.html',
  styleUrls: ['./helm-details.component.scss']
})
export class HelmDetailsComponent implements OnInit {
  chart: any;
  image: string;

  constructor(public dialog: MatDialog,
              private route : ActivatedRoute,
              private router : Router,
              private location: Location) {}

  ngOnInit(): void {
    this.chart = history.state.chart;
    this.image = history.state.image;
  }
  goBack(): void {
    this.location.back();
  }
  openDialogTemplate(): void {
    const dialogRef = this.dialog.open(Charttemplate2Component, {
      width: '800px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
  openDialog(chartName: string): void {
    const dialogRef = this.dialog.open(CharttemplateComponent, {
      width: '800px',
      data: { chart: chartName}
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  searchAndNavigate(keyword: string): void {
    if (keyword.trim() !== '') {
      this.router.navigate(['/helm'], { queryParams: { search: keyword.trim() } });
    }
  }
}
