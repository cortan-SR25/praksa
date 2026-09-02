export type Role='ADMIN'|'USER';
export interface Session { accessToken:string; tokenType:string; expiresAt:string; userId:number; username:string; firstName:string; lastName:string; role:Role; }
export interface Device { id:number; serviceUnitId:number; responsibleUserId:number; name:string; hostname:string|null; ipAddress:string|null; serialNumber:string|null; manufacturer:string|null; model:string|null; deviceType:string; status:string; serviceUnitName:string; responsibleUserName:string; }
export interface License { id:number; softwareId:number; softwareName:string; licenseKey:string|null; licenseType:string; startDate:string; endDate:string|null; quantity:number; purchasePrice:number|null; notes:string|null; computedStatus:string; }
export interface User { id:number; serviceUnitId:number; username:string; firstName:string; lastName:string; email:string; role:Role; active:boolean; serviceUnitName:string; }
export interface ServiceUnit { id:number; organizationalUnitId:number; organizationalUnitName:string; name:string; description:string|null; }
export interface DeviceRequest { serviceUnitId:number; responsibleUserId:number; name:string; hostname:string|null; ipAddress:string|null; serialNumber:string|null; manufacturer:string|null; model:string|null; deviceType:string; status:string; }
export interface UserCreateRequest { serviceUnitId:number; username:string; password:string; firstName:string; lastName:string; email:string; role:Role; }
export interface UserUpdateRequest extends Omit<UserCreateRequest,'password'> { active:boolean; }
export interface Renewal { id:number; licenseId:number; renewedByUserId:number; renewedByName:string; previousEndDate:string; newEndDate:string; renewedAt:string; note:string|null; }
export interface Company { id:number; name:string; address:string|null; description:string|null; }
export interface OrganizationalUnit { id:number; companyId:number; companyName:string; name:string; description:string|null; }
export interface Software { id:number; name:string; vendor:string; version:string; softwareType:string; description:string|null; }
export interface Installation { id:number; deviceId:number; deviceName:string; softwareId:number; softwareName:string; licenseId:number|null; installationDate:string|null; installedVersion:string|null; status:string; }
