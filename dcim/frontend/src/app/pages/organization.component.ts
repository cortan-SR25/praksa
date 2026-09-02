import { Component,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { forkJoin } from 'rxjs';
import { ApiService } from '../core/api.service';
import { Company,OrganizationalUnit,ServiceUnit } from '../core/models';

@Component({standalone:true,imports:[CommonModule],template:`
<div class="page-title"><div><small>STRUKTURA</small><h2>Organizacija</h2><p>Kompanije, organizacione i servisne jedinice.</p></div></div>
<div class="structure-grid"><section class="panel structure"><div class="panel-head"><h3>Kompanije</h3><button class="primary small-button" (click)="editCompany()">+ Dodaj</button></div><button class="structure-item" *ngFor="let c of companies" [class.selected]="c.id===companyId" (click)="selectCompany(c.id)"><span><b>{{c.name}}</b><small>{{c.address||'Adresa nije uneta'}}</small></span><span class="row-actions"><i (click)="$event.stopPropagation();editCompany(c)">✎</i><i (click)="$event.stopPropagation();removeCompany(c)">×</i></span></button></section>
<section class="panel structure"><div class="panel-head"><h3>Organizacione jedinice</h3><button class="primary small-button" [disabled]="!companyId" (click)="editOrganization()">+ Dodaj</button></div><button class="structure-item" *ngFor="let o of filteredOrganizations" [class.selected]="o.id===organizationId" (click)="selectOrganization(o.id)"><span><b>{{o.name}}</b><small>{{o.description||o.companyName}}</small></span><span class="row-actions"><i (click)="$event.stopPropagation();editOrganization(o)">✎</i><i (click)="$event.stopPropagation();removeOrganization(o)">×</i></span></button><div class="empty" *ngIf="companyId&&!filteredOrganizations.length">Nema organizacionih jedinica.</div></section>
<section class="panel structure"><div class="panel-head"><h3>Servisne jedinice</h3><button class="primary small-button" [disabled]="!organizationId" (click)="editService()">+ Dodaj</button></div><div class="structure-item static" *ngFor="let s of filteredServices"><span><b>{{s.name}}</b><small>{{s.description||s.organizationalUnitName}}</small></span><span class="row-actions"><i (click)="editService(s)">✎</i><i (click)="removeService(s)">×</i></span></div><div class="empty" *ngIf="organizationId&&!filteredServices.length">Nema servisnih jedinica.</div></section></div>`})
export class OrganizationComponent implements OnInit {
 companies:Company[]=[];organizations:OrganizationalUnit[]=[];services:ServiceUnit[]=[];companyId:number|null=null;organizationId:number|null=null;
 constructor(private api:ApiService){}
 get filteredOrganizations():OrganizationalUnit[]{return this.organizations.filter(o=>o.companyId===this.companyId);}
 get filteredServices():ServiceUnit[]{return this.services.filter(s=>s.organizationalUnitId===this.organizationId);}
 ngOnInit():void{this.load();}
 load():void{forkJoin({companies:this.api.companies(),organizations:this.api.organizationalUnits(),services:this.api.serviceUnits()}).subscribe(v=>{this.companies=v.companies;this.organizations=v.organizations;this.services=v.services;if(this.companyId&&!this.companies.some(c=>c.id===this.companyId))this.companyId=null;if(this.organizationId&&!this.organizations.some(o=>o.id===this.organizationId))this.organizationId=null;});}
 selectCompany(id:number):void{this.companyId=id;this.organizationId=null;}
 selectOrganization(id:number):void{this.organizationId=id;}
 editCompany(item?:Company):void{const name=prompt('Naziv kompanije',item?.name??'');if(!name)return;const address=prompt('Adresa',item?.address??'')??'';const description=prompt('Opis',item?.description??'')??'';this.api.saveCompany({name,address,description},item?.id).subscribe({next:()=>this.load(),error:e=>alert(e.error?.message??'Čuvanje nije uspelo.')});}
 editOrganization(item?:OrganizationalUnit):void{if(!this.companyId&&!item)return;const name=prompt('Naziv organizacione jedinice',item?.name??'');if(!name)return;const description=prompt('Opis',item?.description??'')??'';this.api.saveOrganizationalUnit({companyId:item?.companyId??this.companyId!,name,description},item?.id).subscribe({next:()=>this.load(),error:e=>alert(e.error?.message??'Čuvanje nije uspelo.')});}
 editService(item?:ServiceUnit):void{if(!this.organizationId&&!item)return;const name=prompt('Naziv servisne jedinice',item?.name??'');if(!name)return;const description=prompt('Opis',item?.description??'')??'';this.api.saveServiceUnit({organizationalUnitId:item?.organizationalUnitId??this.organizationId!,name,description},item?.id).subscribe({next:()=>this.load(),error:e=>alert(e.error?.message??'Čuvanje nije uspelo.')});}
 removeCompany(item:Company):void{if(confirm(`Obrisati kompaniju „${item.name}“?`))this.api.deleteCompany(item.id).subscribe({next:()=>this.load(),error:e=>alert(e.error?.message)});}
 removeOrganization(item:OrganizationalUnit):void{if(confirm(`Obrisati jedinicu „${item.name}“?`))this.api.deleteOrganizationalUnit(item.id).subscribe({next:()=>this.load(),error:e=>alert(e.error?.message)});}
 removeService(item:ServiceUnit):void{if(confirm(`Obrisati servisnu jedinicu „${item.name}“?`))this.api.deleteServiceUnit(item.id).subscribe({next:()=>this.load(),error:e=>alert(e.error?.message)});}
}
