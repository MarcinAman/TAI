export interface GraphQLResponse<T> {
  data: T
}

export interface Currencies {
  currencies: Currency[]
}

export interface Currency {
  code?: string,
  currencyName?: string
}
