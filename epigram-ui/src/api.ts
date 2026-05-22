import type { EpigramDto } from './types'

export async function getRandom(): Promise<EpigramDto> {
  const res = await fetch('/api/epigrams/random')
  if (!res.ok) throw new Error(`${res.status}`)
  return res.json()
}

export interface EpigramRequest {
  text: string
  author: string | null
  source: string | null
}

export async function createEpigram(req: EpigramRequest): Promise<EpigramDto> {
  const res = await fetch('/api/epigrams/', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(req),
  })
  if (!res.ok) throw Object.assign(new Error(), { status: res.status })
  return res.json()
}
