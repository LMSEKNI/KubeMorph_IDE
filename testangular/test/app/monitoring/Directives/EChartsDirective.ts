import { Directive, Input, ElementRef, OnChanges, SimpleChanges } from '@angular/core';
import * as echarts from 'echarts';
import { EChartsOption } from 'echarts';

@Directive({
  selector: '[appEcharts]'
})
export class EChartsDirective implements OnChanges {
  @Input() appEcharts: EChartsOption;

  private chartInstance: echarts.ECharts | null = null;

  constructor(private elementRef: ElementRef) {}

  ngOnChanges(changes: SimpleChanges): void {
    if ('appEcharts' in changes) {
      if (this.chartInstance) {
        this.chartInstance.dispose();
      }
      this.chartInstance = echarts.init(this.elementRef.nativeElement);
      if (this.appEcharts) {
        this.chartInstance.setOption(this.appEcharts);
      }
    }
  }
}
