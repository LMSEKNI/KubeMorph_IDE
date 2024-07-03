import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {CharttemplateComponent} from '../../charttemplate/charttemplate.component';
import {ReleaseServiceService} from '../services/release-service.service';
import { DeleteReleaseComponent} from '../delete-release/delete-release.component';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-release-details',
  templateUrl: './release-details.component.html',
  styleUrls: ['./release-details.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ReleaseDetailsComponent implements OnInit {

  constructor(public dialog: MatDialog,
              private route: ActivatedRoute,
              private router: Router,
              private releaseService: ReleaseServiceService,
              private snackBar: MatSnackBar) {}

  release: any;
  image: string;
  selectedReleaseStatus = '';
  rollbackMessage: string;

  ngOnInit(): void {
    this.release = history.state.release;
    this.image = history.state.image;
    this.getReleaseStatus(this.release.name);
  }
  getReleaseStatus(release: string) {
    this.releaseService.getReleaseStatus(release).subscribe(data => {
        this.selectedReleaseStatus = data;
        console.log('Release Status:', this.selectedReleaseStatus);
      }, error => {
        console.error('Error fetching release status', error);
      }
    );
  }
  openDialog(releaseName: string): void {
    const dialogRef = this.dialog.open(DeleteReleaseComponent, {
      width: '300px',
      data: { release: releaseName }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  searchAndNavigate(keyword: string): void {
    if (keyword.trim() !== '') {
      this.router.navigate(['/helm-release'], { queryParams: { search: keyword.trim() } });
    }
  }
  rollbackRelease(releaseName: string, version?: number): void {
    this.releaseService.rollbackRelease(releaseName, version).subscribe(
      response => {
        if (response.includes('Rollback initiated for release')) {
          this.openSnackBar(response, 'success-snackbar');
        } else if (response.includes('Error: release has no previous version to rollback to')) {
          this.openSnackBar(response, 'error-snackbar');
        }
      },
      error => {
        console.error('Error initiating rollback:', error);
        this.openSnackBar('Failed to initiate rollback: ' + error.message, 'error-snackbar');
      }
    );
  }
  openSnackBar(message: string, panelClass: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: [panelClass]
    });
  }
}
