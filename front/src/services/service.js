import store from "../Store/store";
export const API_URL = 'http://localhost:32810/'

const token = store.getState().user.token;


export async function makeRequest(url, method, body = null) {
    const headers = {
        'Content-Type': 'application/json',
    };

    if(token && token!=='') headers['Authorization'] = 'Bearer ' + token;

    let response;
    if(method === 'POST'){
        console.log(JSON.stringify(body));
        response = await fetch(url, {
            method,
            headers,
            body: JSON.stringify(body),
        });
    } else {
        response = await fetch(url, {
            method,
            headers,
        });
    }
    //console.log(response.body);
    //console.log(response.json());

    const data = await response.json();

    if(data){
        return data
    } else {
        throw new Error(`Error: ${data.status} ${data.message}`);
    }
    /*console.log(response);
    console.log(response.token);
    const data = await response.json();
    console.log(data);
    if (!response.ok) {
        throw new Error(`Error: ${response.status} ${data.message}`);
    }

    return data;
*/
}