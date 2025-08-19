import {BACKEND_URL, GOOGLE_MAPS_API_KEY} from "../configuration.js";

export const securedURL = `${BACKEND_URL}/api`;
export const patientURL = `${securedURL}/patient`;
export const hospitalURL = `${securedURL}/hospital`;
export const doctorURL = `${securedURL}/doctor`;
export const pharmacyURL = `${securedURL}/pharmacy`;
export const GOOGLE_API_KEY = `${GOOGLE_MAPS_API_KEY}`;