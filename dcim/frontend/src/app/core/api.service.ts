import { Injectable } from '@angular/core'; import { HttpClient } from '@angular/common/http'; import { Observable } from 'rxjs'; import { Company,Device,DeviceRequest,Installation,License,OrganizationalUnit,Renewal,ServiceUnit,Software,User,UserCreateRequest,UserUpdateRequest } from './models';
@Injectable({providedIn:'root'}) export class ApiService {
 constructor(private http:HttpClient){}
 devices():Observable<Device[]>{return this.http.get<Device[]>('/api/devices');}
 device(id:number):Observable<Device>{return this.http.get<Device>(`/api/devices/${id}`);}
 licenses():Observable<License[]>{return this.http.get<License[]>('/api/licenses');}
 saveLicense(value:{softwareId:number;licenseKey:string|null;licenseType:string;startDate:string;endDate:string|null;quantity:number;purchasePrice:number|null;notes:string},id?:number):Observable<License>{return id?this.http.put<License>(`/api/licenses/${id}`,value):this.http.post<License>('/api/licenses',value);}
 deleteLicense(id:number):Observable<void>{return this.http.delete<void>(`/api/licenses/${id}`);}
 users():Observable<User[]>{return this.http.get<User[]>('/api/users');}
 serviceUnits():Observable<ServiceUnit[]>{return this.http.get<ServiceUnit[]>('/api/service-units');}
 createDevice(value:DeviceRequest):Observable<Device>{return this.http.post<Device>('/api/devices',value);}
 updateDevice(id:number,value:DeviceRequest):Observable<Device>{return this.http.put<Device>(`/api/devices/${id}`,value);}
 deleteDevice(id:number):Observable<void>{return this.http.delete<void>(`/api/devices/${id}`);}
 createUser(value:UserCreateRequest):Observable<User>{return this.http.post<User>('/api/users',value);}
 updateUser(id:number,value:UserUpdateRequest):Observable<User>{return this.http.put<User>(`/api/users/${id}`,value);}
 deleteUser(id:number):Observable<void>{return this.http.delete<void>(`/api/users/${id}`);}
 renewLicense(id:number,newEndDate:string,note:string):Observable<Renewal>{return this.http.post<Renewal>(`/api/licenses/${id}/renewals`,{newEndDate,note});}
 renewalHistory(id:number):Observable<Renewal[]>{return this.http.get<Renewal[]>(`/api/licenses/${id}/renewals`);}
 companies():Observable<Company[]>{return this.http.get<Company[]>('/api/companies');}
 saveCompany(value:{name:string;address:string;description:string},id?:number):Observable<Company>{return id?this.http.put<Company>(`/api/companies/${id}`,value):this.http.post<Company>('/api/companies',value);}
 deleteCompany(id:number):Observable<void>{return this.http.delete<void>(`/api/companies/${id}`);}
 organizationalUnits(companyId?:number):Observable<OrganizationalUnit[]>{return this.http.get<OrganizationalUnit[]>('/api/organizational-units',{params:companyId?{companyId}: {}});}
 saveOrganizationalUnit(value:{companyId:number;name:string;description:string},id?:number):Observable<OrganizationalUnit>{return id?this.http.put<OrganizationalUnit>(`/api/organizational-units/${id}`,value):this.http.post<OrganizationalUnit>('/api/organizational-units',value);}
 deleteOrganizationalUnit(id:number):Observable<void>{return this.http.delete<void>(`/api/organizational-units/${id}`);}
 saveServiceUnit(value:{organizationalUnitId:number;name:string;description:string},id?:number):Observable<ServiceUnit>{return id?this.http.put<ServiceUnit>(`/api/service-units/${id}`,value):this.http.post<ServiceUnit>('/api/service-units',value);}
 deleteServiceUnit(id:number):Observable<void>{return this.http.delete<void>(`/api/service-units/${id}`);}
 software():Observable<Software[]>{return this.http.get<Software[]>('/api/software');}
 saveSoftware(value:{name:string;vendor:string;version:string;softwareType:string;description:string},id?:number):Observable<Software>{return id?this.http.put<Software>(`/api/software/${id}`,value):this.http.post<Software>('/api/software',value);}
 deleteSoftware(id:number):Observable<void>{return this.http.delete<void>(`/api/software/${id}`);}
 installations(deviceId:number):Observable<Installation[]>{return this.http.get<Installation[]>('/api/installations',{params:{deviceId}});}
 createInstallation(value:{deviceId:number;softwareId:number;licenseId:number|null;installationDate:string;installedVersion:string;status:string}):Observable<Installation>{return this.http.post<Installation>('/api/installations',value);}
 deleteInstallation(id:number):Observable<void>{return this.http.delete<void>(`/api/installations/${id}`);}
}
