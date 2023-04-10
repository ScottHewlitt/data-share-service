import http from 'k6/http';
import { sleep, check } from 'k6';

const host = __ENV.HOST || 'http://localhost:8080';

export const options = {
  scenarios: {
    common_scenario: {
      // name of the executor to use
      executor: 'shared-iterations',

      // common scenario configuration
      startTime: '5s',
    }
  },
};

export default function (data) {
  const res = http.get(`${host}/healthy`);
  check(res, { 'status was 204': (r) => r.status == 204 });
}