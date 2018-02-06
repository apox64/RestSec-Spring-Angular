export interface AttackableEndpoint {
  id: string,
  endpointUrl: string,
  httpVerb: string,
  authToken: string,
  scanStatus: boolean
}
