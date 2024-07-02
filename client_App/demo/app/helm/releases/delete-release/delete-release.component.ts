import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import { ReleaseServiceService} from '../services/release-service.service';

@Component({
  selector: 'app-delete-release',
  templateUrl: './delete-release.component.html',
  styleUrls: ['./delete-release.component.scss']
})
export class DeleteReleaseComponent implements OnInit {
  @Input() release: string | null = null;
  constructor(public dialogRef: MatDialogRef<DeleteReleaseComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { release: string },
              private releaseService: ReleaseServiceService) {}

  ngOnInit(): void {
  }
  deleteRelease(): void {
    this.releaseService.deleteRelease(this.data.release)
      .subscribe(
        response => {
          alert('Successfully uninstalled release: ' + this.data.release);
          this.dialogRef.close(true);
        },
        error => {
          alert('Failed to uninstall release: ' + error.message);
        }
      );
  }
  closeDialog(): void {
    this.dialogRef.close();
  }

}
