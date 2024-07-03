import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ReleaseServiceService} from './services/release-service.service';
import {Location} from '@angular/common';

@Component({
  selector: 'app-releases',
  templateUrl: './releases.component.html',
  styleUrls: ['./releases.component.scss']
})
export class ReleasesComponent implements OnInit {

  constructor(
    private releaseService: ReleaseServiceService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) { }



  images: string[] = [
    '/assets/images/logo_card1.png',
    '/assets/images/logo_card2.png',
    '/assets/images/logo_card3.png',
    '/assets/images/logo_card4.png',
    '/assets/images/logo_card5.png',
    '/assets/images/logo_card.png',
  ];
  loading = false;
  searchResults: String [] = [];
  chart: string | null = null;
  releases: any;
  filteredReleases: any[] = [];
  query: string = '';

  ngOnInit() {
    this.fetchReleases();
    this.route.queryParams.subscribe(params => {
      const searchParam = params['search'];
      if (searchParam) {
        this.query = searchParam;
        this.filterReleases(searchParam);
      }
    });
  }
  goBack(): void {
    this.location.back();
  }
  fetchReleases() {
    this.loading = true;
    this.releaseService.listReleases().subscribe(data => {
      this.releases = data;
      this.loading = false;
      if (this.query) {
        this.filterReleases(this.query);
      }
    }, error => {
      this.loading = false;
      console.error('Error fetching releases', error);
    });
  }

  filterReleases(query: string) {
    if (query.trim() !== '') {
      this.filteredReleases = this.releases.filter(release =>
        release.name.toLowerCase().includes(query.toLowerCase())
      );
    }
  }
  getImage(index: number): string {
    return this.images[index % this.images.length];
  }

  navigateToDetail(release: any): void {
    const image = this.getImage(this.searchResults.indexOf(release.name));
    this.router.navigate(['/release-details'], { state: { release: release, image: image } });
  }
}
