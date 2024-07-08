import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {MatTabsModule} from '@angular/material/tabs';
import {ActivatedRoute, Router} from '@angular/router';
import {HelmServicesService} from '../services/helm-services.service';
import { Location } from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {AddChartComponent} from '../add-chart/add-chart.component';

@Component({
  selector: 'app-helm',
  templateUrl: './helm.component.html',
  styleUrls: ['./helm.component.scss']
})
export class HelmComponent implements OnInit {
  longText = `The Shiba Inu is the smallest of the six original and distinct spitz breeds of dog
  from Japan. A small, agile dog that copes very well with mountainous terrain, the Shiba Inu was
  originally bred for hunting.`;

  images: string[] = [
    '/assets/images/logo_card1.png',
    '/assets/images/logo_card2.png',
    '/assets/images/logo_card3.png',
    '/assets/images/logo_card4.png',
    '/assets/images/logo_card5.png',
    '/assets/images/logo_card.png',
  ];

  pageSizeOptions: number[] = [10, 50, 100];
  pageSize = 10; // Default page size
  currentPage = 0;
  loading = false;
  searchResults: String [] = [];
  keyword = '';
  chart: string | null = null;
  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
  }

  constructor(private  router: Router,
              private route: ActivatedRoute,
              private helmService: HelmServicesService,
              private location: Location,
              public dialog: MatDialog) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const searchParam = params['search'];
      if (searchParam) {
        this.keyword = searchParam;
        setTimeout(() => {
          this.search(searchParam);
        });
      }
    });
  }
  openAddChartDialog(): void {
    const dialogRef = this.dialog.open(AddChartComponent, {
      width: '500px'
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }
  goBack(): void {
    this.location.back();
  }
  navigateToDetail(chart: any): void {
    const image = this.getImage(this.searchResults.indexOf(chart)); // Get corresponding image URL
    this.router.navigate(['/helm-details'], { state: { chart: chart, image: image } });
  }
  get pagedSearchResults(): any[] {
    const startIndex = this.currentPage * this.pageSize;
    return this.searchResults.slice(startIndex, startIndex + this.pageSize);
  }
  search(keyword: string): void {
    if (keyword.trim() !== '') {
      this.loading = true; // Show spinner
      this.helmService.searchRepo(keyword.trim()).subscribe(
        results => {
          this.searchResults = results;
          console.log(results); // Check the console for search results
          this.loading = false; // Hide spinner after loading
        },
        error => {
          console.error('Failed to fetch search results:', error);
          this.loading = false; // Hide spinner in case of error
        }
      );
    }
  }
  getImage(index: number): string {
    return this.images[index % this.images.length];
  }

}
