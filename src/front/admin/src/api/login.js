import fetch from '@/utils/fetch'

export function login(username, password) {
  return fetch({
    url: '/api/token/auth',
    method: 'post',
    params: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return fetch({
    url: '/api/token/user',
    method: 'get',
    params: { token }
  })
}

export function refreshToken(token) {
  return fetch({
    url: '/api/token/refresh',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return fetch({
    url: '/user/logout',
    method: 'post'
  })
}
