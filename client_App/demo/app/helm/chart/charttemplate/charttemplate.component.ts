import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ReleaseServiceService} from '../../releases/services/release-service.service';

@Component({
  selector: 'app-charttemplate',
  templateUrl: './charttemplate.component.html',
  styleUrls: ['./charttemplate.component.scss']
})
export class CharttemplateComponent implements OnInit {
  @Input() release: string | null = null;
  constructor(public dialogRef: MatDialogRef<CharttemplateComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { chart: string },
              ) {}

  ngOnInit(): void {
  }
  closeDialog(): void {
    this.dialogRef.close();
  }
}
