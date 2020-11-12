import { Inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DATE_FORMAT, SERVER_API_URL_CONFIG, ServerApiUrlConfig } from '@lamis/web-core';
import { map } from 'rxjs/operators';
import { Laboratory, LaboratoryLine, LabTest, LabTestCategory, Patient } from '../model/laboratory.model';
import * as moment_ from 'moment';
import { Moment } from 'moment';

const moment = moment_;

type EntityResponseType = HttpResponse<Laboratory>;
type EntityArrayResponseType = HttpResponse<Laboratory[]>;

@Injectable({providedIn: 'root'})
export class LaboratoryService {
    public resourceUrl = '';

    constructor(protected http: HttpClient, @Inject(SERVER_API_URL_CONFIG) private serverUrl: ServerApiUrlConfig) {
        this.resourceUrl = serverUrl.SERVER_API_URL + '/api/laboratories';
    }

    create(laboratory: Laboratory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(laboratory);
        return this.http
            .post<Laboratory>(this.resourceUrl, copy, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(laboratory: Laboratory): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(laboratory);
        return this.http
            .put<Laboratory>(this.resourceUrl, copy, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<Laboratory>(`${this.resourceUrl}/${id}`, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findByUuid(id: string): Observable<EntityResponseType> {
        return this.http
            .get<Laboratory>(`${this.resourceUrl}/by-uuid/${id}`, {observe: 'response'})
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    getPatient(id: any) {
        return this.http.get<Patient>(`/api/patients/by-uuid/${id}`, {observe: 'body'})
            .pipe(map((res) => {
                if (res) {
                    res.dateRegistration = res.dateRegistration != null ? moment(res.dateRegistration) : null;
                }
                return res;
            }));
    }

    getVisitDatesByPatient(patientId: number) {
        return this.http.get<Moment[]>(`${this.resourceUrl}/patient/${patientId}/report-dates`)
            .pipe(map((res) => {
                    res.forEach(d => moment(d));
                    return res;
                })
            );
    }

    laboratoryCategories() {
        return this.http.get<LabTestCategory[]>(`${this.resourceUrl}/test-categories`);
    }

    getLinesByLaboratory(laboratoryId: number) {
        return this.http.get<LaboratoryLine[]>(`${this.resourceUrl}/${laboratoryId}/lines`);
    }

    labTestsByCategory(id: number) {
        return this.http.get<LabTest[]>(`${this.resourceUrl}/lab-tests/category/${id}`);
    }

    getLabTestById(id) {
        return this.http.get<LabTest>(`${this.resourceUrl}/lab-test/${id}`);
    }

    latestVisit(patientId: number) {
        return this.http.get<Laboratory>(`${this.resourceUrl}/patient/${patientId}/latest`);
    }

    protected convertDateFromClient(laboratory: Laboratory): Laboratory {
        const copy: Laboratory = Object.assign({}, laboratory, {
            dateResultReceived: laboratory.dateResultReceived != null && laboratory.dateResultReceived.isValid() ? laboratory.dateResultReceived.format(DATE_FORMAT) : null,
            dateAssay: laboratory.dateAssay != null && laboratory.dateAssay.isValid() ? laboratory.dateAssay.format(DATE_FORMAT) : null,
            dateSampleCollected: laboratory.dateSampleCollected != null && laboratory.dateSampleCollected.isValid() ? laboratory.dateSampleCollected.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.dateSampleCollected = res.body.dateSampleCollected != null ? moment(res.body.dateSampleCollected) : null;
            res.body.dateResultReceived = res.body.dateResultReceived != null ? moment(res.body.dateResultReceived) : null;
            res.body.dateAssay = res.body.dateAssay != null ? moment(res.body.dateAssay) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((laboratory: Laboratory) => {
                laboratory.dateResultReceived = laboratory.dateResultReceived != null ? moment(laboratory.dateResultReceived) : null;
                laboratory.dateAssay = laboratory.dateAssay != null ? moment(laboratory.dateAssay) : null;
                laboratory.dateSampleCollected = laboratory.dateSampleCollected != null ? moment(laboratory.dateSampleCollected) : null;
                1;
            });
        }
        return res;
    }
}
