import {BACKEND_URL, GOOGLE_MAPS_API_KEY} from "../configuration.js";

export const URL = BACKEND_URL;
export const IMAGE_URL = `${URL}/get/profile?image=`;
export const displayImage = (imagePath) => {
    return `${URL}/get/profile?image=${imagePath}`;
}
export const securedURL = `${URL}/api`;
export const patientURL = `${securedURL}/patient`;
export const hospitalURL = `${securedURL}/hospital`;
export const doctorURL = `${securedURL}/doctor`;
export const pharmacyURL = `${securedURL}/pharmacy`;
export const GOOGLE_API_KEY = `${GOOGLE_MAPS_API_KEY}`;