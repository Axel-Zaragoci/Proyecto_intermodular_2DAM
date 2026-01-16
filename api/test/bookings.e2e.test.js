const BASE_URL = 'http://localhost:3000/booking'

describe('Bookings API (external)', () => {
  let bookingId

  async function assertWithResponse(res, assertion) {
    try {
      await assertion()
    } catch (err) {
      let bodyText = ''
      try {
        bodyText = await res.clone().text()
      } catch (_) {
        bodyText = '<no se pudo leer el cuerpo>'
      }
      console.error(
        `\n[API FAIL] ${res.url || ''} → ${res.status} ${
          res.statusText || ''
        }\n` +
          `Headers: ${[...res.headers.entries()]
            .map(([k, v]) => `${k}: ${v}`)
            .join(', ')}\n` +
          `Body:\n${bodyText}\n`,
      )
      throw err
    }
  }

  const validBooking = {
    userID: '69681f108598651dba187c8e',
    roomID: '69681ef58598651dba187c8a',
    checkInDate: '15/03/2026',
    checkOutDate: '16/03/2026',
    payDate: '14/01/2026',
    guests: 2,
  }

  test('GET /bookings → 200', async () => {
    const res = await fetch(BASE_URL)
    await assertWithResponse(res, () => {
      expect(res.status).toBe(200)
    })
  })

  test('POST /bookings → 201', async () => {
    const res = await fetch(BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(validBooking),
    })

    await assertWithResponse(res, () => {
      expect(res.status).toBe(201)
    })

    const body = await res.json().catch(() => ({}))
    bookingId = body.id || body._id
  })

  test('POST /bookings invalid → 400', async () => {
    const res = await fetch(BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: '{}',
    })

    await assertWithResponse(res, () => {
      expect(res.status).toBe(400)
    })
  })

  test('GET /bookings/:id → 200', async () => {
    const res = await fetch(`${BASE_URL}/${bookingId}`)
    await assertWithResponse(res, () => {
      expect(res.status).toBe(200)
    })
  })

  test('GET /bookings/:id invalid → 400', async () => {
    const res = await fetch(`${BASE_URL}/invalid-id`)
    await assertWithResponse(res, () => {
      expect(res.status).toBe(400)
    })
  })

  test('PATCH /bookings/:id → 200', async () => {
    const res = await fetch(`${BASE_URL}/${bookingId}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...validBooking, occupants: 1 }),
    })

    await assertWithResponse(res, () => {
      expect(res.status).toBe(200)
    })
  })

  test('PATCH /bookings/:id/cancel → 200', async () => {
    const res = await fetch(`${BASE_URL}/${bookingId}/cancel`, {
      method: 'PATCH',
    })

    await assertWithResponse(res, () => {
      expect(res.status).toBe(200)
    })
  })

  test('DELETE /bookings/:id → 200 | 204', async () => {
    const res = await fetch(`${BASE_URL}/${bookingId}`, {
      method: 'DELETE',
    })

    await assertWithResponse(res, () => {
      expect([200, 204]).toContain(res.status)
    })
  })

  test('DELETE /bookings/:id not found → 404', async () => {
    const res = await fetch(`${BASE_URL}/${bookingId}`, {
      method: 'DELETE',
    })

    await assertWithResponse(res, () => {
      expect(res.status).toBe(404)
    })
  })
})
