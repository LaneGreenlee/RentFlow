import { useState } from 'react'
import './App.css'

const API_BASE = import.meta.env.VITE_API_BASE || '/api'

const PROPERTY_TYPES = ['SINGLE_FAMILY', 'DUPLEX', 'APARTMENT', 'CONDO']
const EMPLOYMENT_STATUSES = ['EMPLOYED', 'SELF_EMPLOYED', 'UNEMPLOYED', 'RETIRED']
const LEASE_STATUSES = ['PENDING', 'ACTIVE', 'EXPIRED', 'TERMINATED']
const PAYMENT_TYPES = ['RENT', 'SECURITY_DEPOSIT', 'LATE_FEE', 'UTILITY', 'OTHER']
const PAYMENT_METHODS = ['CASH', 'CHECK', 'BANK_TRANSFER', 'CREDIT_CARD', 'VENMO', 'ZELLE']
const PAYMENT_STATUSES = ['PENDING', 'COMPLETED', 'FAILED', 'REFUNDED']
const MAINTENANCE_STATUSES = ['OPEN', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED']
const PRIORITIES = ['LOW', 'MEDIUM', 'HIGH', 'URGENT']

const emptyToNull = (value) => (value === '' ? null : value)
const toInput = (value) => (value === null || value === undefined ? '' : String(value))
const toInt = (value) => (value === '' ? null : Number.parseInt(value, 10))
const toFloat = (value) => (value === '' ? null : Number.parseFloat(value))

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  })

  const contentType = response.headers.get('content-type') || ''
  const payload = contentType.includes('application/json') ? await response.json() : await response.text()

  if (!response.ok) {
    const message = typeof payload === 'string' ? payload : JSON.stringify(payload)
    throw new Error(`${response.status} ${response.statusText}: ${message}`)
  }

  return payload
}

function App() {
  const [activePage, setActivePage] = useState('dashboard')
  const [activeModal, setActiveModal] = useState(null)
  const [output, setOutput] = useState({ title: 'Ready', data: null, error: null })

  const [propertiesList, setPropertiesList] = useState([])
  const [tenantsList, setTenantsList] = useState([])
  const [leasesList, setLeasesList] = useState([])
  const [paymentsList, setPaymentsList] = useState([])
  const [maintenanceList, setMaintenanceList] = useState([])

  const [propertyCreate, setPropertyCreate] = useState({
    address: '',
    city: '',
    state: '',
    zipCode: '',
    propertyType: 'APARTMENT',
    bedrooms: '',
    bathrooms: '',
    squareFeet: '',
    monthlyRent: '',
    purchaseDate: '',
    purchasePrice: '',
  })
  const [propertyUpdate, setPropertyUpdate] = useState({ id: '', ...propertyCreate })
  const [propertyId, setPropertyId] = useState('')
  const [propertyType, setPropertyType] = useState('APARTMENT')
  const [propertyRentRange, setPropertyRentRange] = useState({ min: '', max: '' })

  const [tenantCreate, setTenantCreate] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    dateOfBirth: '',
    ssnLastFour: '',
    employmentStatus: 'EMPLOYED',
    monthlyIncome: '',
    emergencyContactName: '',
    emergencyContactPhone: '',
  })
  const [tenantUpdate, setTenantUpdate] = useState({ id: '', ...tenantCreate })
  const [tenantId, setTenantId] = useState('')
  const [tenantEmail, setTenantEmail] = useState('')
  const [tenantPhone, setTenantPhone] = useState('')
  const [tenantEmployment, setTenantEmployment] = useState('EMPLOYED')
  const [tenantSearch, setTenantSearch] = useState('')

  const [leaseCreate, setLeaseCreate] = useState({
    propertyId: '',
    tenantId: '',
    startDate: '',
    endDate: '',
    monthlyRent: '',
    securityDeposit: '',
    leaseStatus: 'PENDING',
    leaseTerms: '',
  })
  const [leaseUpdate, setLeaseUpdate] = useState({ id: '', ...leaseCreate })
  const [leaseId, setLeaseId] = useState('')
  const [leasePropertyId, setLeasePropertyId] = useState('')
  const [leaseTenantId, setLeaseTenantId] = useState('')
  const [leaseStatus, setLeaseStatus] = useState('ACTIVE')
  const [leaseExpiringDays, setLeaseExpiringDays] = useState('30')

  const [paymentCreate, setPaymentCreate] = useState({
    leaseId: '',
    paymentDate: '',
    amount: '',
    paymentType: 'RENT',
    paymentMethod: 'ONLINE',
    paymentStatus: 'PENDING',
    dueDate: '',
    notes: '',
  })
  const [paymentUpdate, setPaymentUpdate] = useState({ id: '', ...paymentCreate })
  const [paymentId, setPaymentId] = useState('')
  const [paymentLeaseId, setPaymentLeaseId] = useState('')
  const [paymentStatus, setPaymentStatus] = useState('COMPLETED')
  const [paymentDateRange, setPaymentDateRange] = useState({ startDate: '', endDate: '' })
  const [paymentTotalLeaseId, setPaymentTotalLeaseId] = useState('')

  const [maintenanceCreate, setMaintenanceCreate] = useState({
    propertyId: '',
    tenantId: '',
    requestDate: '',
    description: '',
    priority: 'MEDIUM',
    status: 'OPEN',
    estimatedCost: '',
    actualCost: '',
    completionDate: '',
    notes: '',
  })
  const [maintenanceUpdate, setMaintenanceUpdate] = useState({ id: '', ...maintenanceCreate })
  const [maintenanceId, setMaintenanceId] = useState('')
  const [maintenancePropertyId, setMaintenancePropertyId] = useState('')
  const [maintenanceStatus, setMaintenanceStatus] = useState('OPEN')
  const [maintenancePriority, setMaintenancePriority] = useState('MEDIUM')

  const setResult = (title, data) => setOutput({ title, data, error: null })
  const setError = (title, error) => setOutput({ title, data: null, error: error.message })

  const buildPropertyPayload = (form) => ({
    address: form.address,
    city: form.city,
    state: form.state,
    zipCode: form.zipCode,
    propertyType: emptyToNull(form.propertyType),
    bedrooms: toInt(form.bedrooms),
    bathrooms: toFloat(form.bathrooms),
    squareFeet: toInt(form.squareFeet),
    monthlyRent: toFloat(form.monthlyRent),
    purchaseDate: emptyToNull(form.purchaseDate),
    purchasePrice: toFloat(form.purchasePrice),
  })

  const buildTenantPayload = (form) => ({
    firstName: form.firstName,
    lastName: form.lastName,
    email: form.email,
    phone: form.phone,
    dateOfBirth: emptyToNull(form.dateOfBirth),
    ssnLastFour: emptyToNull(form.ssnLastFour),
    employmentStatus: emptyToNull(form.employmentStatus),
    monthlyIncome: toFloat(form.monthlyIncome),
    emergencyContactName: emptyToNull(form.emergencyContactName),
    emergencyContactPhone: emptyToNull(form.emergencyContactPhone),
  })

  const buildLeasePayload = (form) => ({
    property: { propertyId: toInt(form.propertyId) },
    tenant: { tenantId: toInt(form.tenantId) },
    startDate: emptyToNull(form.startDate),
    endDate: emptyToNull(form.endDate),
    monthlyRent: toFloat(form.monthlyRent),
    securityDeposit: toFloat(form.securityDeposit),
    leaseStatus: emptyToNull(form.leaseStatus),
    leaseTerms: emptyToNull(form.leaseTerms),
  })

  const buildPaymentPayload = (form) => ({
    lease: { leaseId: toInt(form.leaseId) },
    paymentDate: emptyToNull(form.paymentDate),
    amount: toFloat(form.amount),
    paymentType: emptyToNull(form.paymentType),
    paymentMethod: emptyToNull(form.paymentMethod),
    paymentStatus: emptyToNull(form.paymentStatus),
    dueDate: emptyToNull(form.dueDate),
    notes: emptyToNull(form.notes),
  })

  const buildMaintenancePayload = (form) => ({
    property: { propertyId: toInt(form.propertyId) },
    tenant: form.tenantId ? { tenantId: toInt(form.tenantId) } : null,
    requestDate: emptyToNull(form.requestDate),
    description: form.description,
    priority: emptyToNull(form.priority),
    status: emptyToNull(form.status),
    estimatedCost: toFloat(form.estimatedCost),
    actualCost: toFloat(form.actualCost),
    completionDate: emptyToNull(form.completionDate),
    notes: emptyToNull(form.notes),
  })

  const handleCreateProperty = async () => {
    try {
      const data = await apiRequest('/properties', {
        method: 'POST',
        body: JSON.stringify(buildPropertyPayload(propertyCreate)),
      })
      setResult('Property created', data)
    } catch (error) {
      setError('Property create failed', error)
    }
  }

  const handleUpdateProperty = async () => {
    try {
      const data = await apiRequest(`/properties/${propertyUpdate.id}`, {
        method: 'PUT',
        body: JSON.stringify(buildPropertyPayload(propertyUpdate)),
      })
      setResult('Property updated', data)
      setActiveModal(null)
    } catch (error) {
      setError('Property update failed', error)
    }
  }

  const handleDeleteProperty = async () => {
    try {
      const data = await apiRequest(`/properties/${propertyId}`, { method: 'DELETE' })
      setResult('Property deleted', data)
    } catch (error) {
      setError('Property delete failed', error)
    }
  }

  const handleGetPropertyById = async () => {
    try {
      const data = await apiRequest(`/properties/${propertyId}`)
      setPropertiesList(data ? [data] : [])
      setResult('Property by ID', data)
    } catch (error) {
      setError('Property fetch failed', error)
    }
  }

  const handleGetAllProperties = async () => {
    try {
      const data = await apiRequest('/properties')
      setPropertiesList(Array.isArray(data) ? data : [])
      setResult('All properties', data)
    } catch (error) {
      setError('Properties fetch failed', error)
    }
  }

  const handleGetPropertiesByType = async () => {
    try {
      const data = await apiRequest(`/properties/type/${propertyType}`)
      setPropertiesList(Array.isArray(data) ? data : [])
      setResult('Properties by type', data)
    } catch (error) {
      setError('Properties by type failed', error)
    }
  }

  const handleGetPropertiesByRentRange = async () => {
    try {
      const minRent = propertyRentRange.min === '' ? 0 : propertyRentRange.min
      const maxRent = propertyRentRange.max === '' ? 9999999 : propertyRentRange.max
      const query = `?minRent=${minRent}&maxRent=${maxRent}`
      const data = await apiRequest(`/properties/rent-range${query}`)
      setPropertiesList(Array.isArray(data) ? data : [])
      setResult('Properties by rent range', data)
    } catch (error) {
      setError('Properties by rent range failed', error)
    }
  }

  const handleCreateTenant = async () => {
    try {
      const data = await apiRequest('/tenants', {
        method: 'POST',
        body: JSON.stringify(buildTenantPayload(tenantCreate)),
      })
      setResult('Tenant created', data)
    } catch (error) {
      setError('Tenant create failed', error)
    }
  }

  const handleUpdateTenant = async () => {
    try {
      const data = await apiRequest(`/tenants/${tenantUpdate.id}`, {
        method: 'PUT',
        body: JSON.stringify(buildTenantPayload(tenantUpdate)),
      })
      setResult('Tenant updated', data)
      setActiveModal(null)
    } catch (error) {
      setError('Tenant update failed', error)
    }
  }

  const handleDeleteTenant = async () => {
    try {
      const data = await apiRequest(`/tenants/${tenantId}`, { method: 'DELETE' })
      setResult('Tenant deleted', data)
    } catch (error) {
      setError('Tenant delete failed', error)
    }
  }

  const handleGetTenantById = async () => {
    try {
      const data = await apiRequest(`/tenants/${tenantId}`)
      setTenantsList(data ? [data] : [])
      setResult('Tenant by ID', data)
    } catch (error) {
      setError('Tenant fetch failed', error)
    }
  }

  const handleGetAllTenants = async () => {
    try {
      const data = await apiRequest('/tenants')
      setTenantsList(Array.isArray(data) ? data : [])
      setResult('All tenants', data)
    } catch (error) {
      setError('Tenants fetch failed', error)
    }
  }

  const handleGetTenantByEmail = async () => {
    if (!tenantEmail.trim()) {
      setError('Email required', new Error('Please enter an email address'))
      return
    }
    try {
      const data = await apiRequest(`/tenants/email/${encodeURIComponent(tenantEmail)}`)
      setTenantsList(data ? [data] : [])
      setResult('Tenant by email', data)
    } catch (error) {
      setTenantsList([])
      setError('Tenant by email failed', error)
    }
  }

  const handleGetTenantByPhone = async () => {
    if (!tenantPhone.trim()) {
      setError('Phone required', new Error('Please enter a phone number'))
      return
    }
    try {
      const data = await apiRequest(`/tenants/phone/${encodeURIComponent(tenantPhone)}`)
      setTenantsList(data ? [data] : [])
      setResult('Tenant by phone', data)
    } catch (error) {
      setTenantsList([])
      setError('Tenant by phone failed', error)
    }
  }

  const handleGetTenantsByEmployment = async () => {
    try {
      const data = await apiRequest(`/tenants/employment/${tenantEmployment}`)
      setTenantsList(Array.isArray(data) ? data : [])
      setResult('Tenants by employment', data)
    } catch (error) {
      setError('Tenants by employment failed', error)
    }
  }

  const handleSearchTenants = async () => {
    try {
      const data = await apiRequest(`/tenants/search?name=${encodeURIComponent(tenantSearch)}`)
      setTenantsList(Array.isArray(data) ? data : [])
      setResult('Tenant search results', data)
    } catch (error) {
      setError('Tenant search failed', error)
    }
  }

  const handleCreateLease = async () => {
    try {
      const data = await apiRequest('/leases', {
        method: 'POST',
        body: JSON.stringify(buildLeasePayload(leaseCreate)),
      })
      setResult('Lease created', data)
    } catch (error) {
      setError('Lease create failed', error)
    }
  }

  const handleUpdateLease = async () => {
    try {
      const data = await apiRequest(`/leases/${leaseUpdate.id}`, {
        method: 'PUT',
        body: JSON.stringify(buildLeasePayload(leaseUpdate)),
      })
      setResult('Lease updated', data)
      setActiveModal(null)
    } catch (error) {
      setError('Lease update failed', error)
    }
  }

  const handleDeleteLease = async () => {
    try {
      const data = await apiRequest(`/leases/${leaseId}`, { method: 'DELETE' })
      setResult('Lease deleted', data)
    } catch (error) {
      setError('Lease delete failed', error)
    }
  }

  const handleGetLeaseById = async () => {
    try {
      const data = await apiRequest(`/leases/${leaseId}`)
      setLeasesList(data ? [data] : [])
      setResult('Lease by ID', data)
    } catch (error) {
      setError('Lease fetch failed', error)
    }
  }

  const handleGetAllLeases = async () => {
    try {
      const data = await apiRequest('/leases')
      setLeasesList(Array.isArray(data) ? data : [])
      setResult('All leases', data)
    } catch (error) {
      setError('Leases fetch failed', error)
    }
  }

  const handleGetLeasesByProperty = async () => {
    try {
      const data = await apiRequest(`/leases/property/${leasePropertyId}`)
      setLeasesList(Array.isArray(data) ? data : [])
      setResult('Leases by property', data)
    } catch (error) {
      setError('Leases by property failed', error)
    }
  }

  const handleGetLeasesByTenant = async () => {
    try {
      const data = await apiRequest(`/leases/tenant/${leaseTenantId}`)
      setLeasesList(Array.isArray(data) ? data : [])
      setResult('Leases by tenant', data)
    } catch (error) {
      setError('Leases by tenant failed', error)
    }
  }

  const handleGetLeasesByStatus = async () => {
    try {
      const data = await apiRequest(`/leases/status/${leaseStatus}`)
      setLeasesList(Array.isArray(data) ? data : [])
      setResult('Leases by status', data)
    } catch (error) {
      setError('Leases by status failed', error)
    }
  }

  const handleGetActiveLeases = async () => {
    try {
      const data = await apiRequest('/leases/active')
      setLeasesList(Array.isArray(data) ? data : [])
      setResult('Active leases', data)
    } catch (error) {
      setError('Active leases failed', error)
    }
  }

  const handleGetLeasesExpiringSoon = async () => {
    try {
      const data = await apiRequest(`/leases/expiring-soon?days=${leaseExpiringDays}`)
      setLeasesList(Array.isArray(data) ? data : [])
      setResult('Leases expiring soon', data)
    } catch (error) {
      setError('Expiring leases failed', error)
    }
  }

  const handleCreatePayment = async () => {
    try {
      const data = await apiRequest('/payments', {
        method: 'POST',
        body: JSON.stringify(buildPaymentPayload(paymentCreate)),
      })
      setResult('Payment created', data)
    } catch (error) {
      setError('Payment create failed', error)
    }
  }

  const handleUpdatePayment = async () => {
    try {
      const data = await apiRequest(`/payments/${paymentUpdate.id}`, {
        method: 'PUT',
        body: JSON.stringify(buildPaymentPayload(paymentUpdate)),
      })
      setResult('Payment updated', data)
      setActiveModal(null)
    } catch (error) {
      setError('Payment update failed', error)
    }
  }

  const handleDeletePayment = async () => {
    try {
      const data = await apiRequest(`/payments/${paymentId}`, { method: 'DELETE' })
      setResult('Payment deleted', data)
    } catch (error) {
      setError('Payment delete failed', error)
    }
  }

  const handleGetPaymentById = async () => {
    try {
      const data = await apiRequest(`/payments/${paymentId}`)
      setPaymentsList(data ? [data] : [])
      setResult('Payment by ID', data)
    } catch (error) {
      setError('Payment fetch failed', error)
    }
  }

  const handleGetAllPayments = async () => {
    try {
      const data = await apiRequest('/payments')
      setPaymentsList(Array.isArray(data) ? data : [])
      setResult('All payments', data)
    } catch (error) {
      setError('Payments fetch failed', error)
    }
  }

  const handleGetPaymentsByLease = async () => {
    try {
      const data = await apiRequest(`/payments/lease/${paymentLeaseId}`)
      setPaymentsList(Array.isArray(data) ? data : [])
      setResult('Payments by lease', data)
    } catch (error) {
      setError('Payments by lease failed', error)
    }
  }

  const handleGetPaymentsByStatus = async () => {
    try {
      const data = await apiRequest(`/payments/status/${paymentStatus}`)
      setPaymentsList(Array.isArray(data) ? data : [])
      setResult('Payments by status', data)
    } catch (error) {
      setError('Payments by status failed', error)
    }
  }

  const handleGetOverduePayments = async () => {
    try {
      const data = await apiRequest('/payments/overdue')
      setPaymentsList(Array.isArray(data) ? data : [])
      setResult('Overdue payments', data)
    } catch (error) {
      setError('Overdue payments failed', error)
    }
  }

  const handleGetPaymentsByDateRange = async () => {
    try {
      const query = `?startDate=${paymentDateRange.startDate}&endDate=${paymentDateRange.endDate}`
      const data = await apiRequest(`/payments/date-range${query}`)
      setPaymentsList(Array.isArray(data) ? data : [])
      setResult('Payments by date range', data)
    } catch (error) {
      setError('Payments by date range failed', error)
    }
  }

  const handleGetPaymentsTotalForLease = async () => {
    try {
      const data = await apiRequest(`/payments/lease/${paymentTotalLeaseId}/total`)
      setResult('Payments total for lease', data)
    } catch (error) {
      setError('Payments total failed', error)
    }
  }

  const handleCreateMaintenance = async () => {
    try {
      const data = await apiRequest('/maintenance-requests', {
        method: 'POST',
        body: JSON.stringify(buildMaintenancePayload(maintenanceCreate)),
      })
      setResult('Maintenance request created', data)
    } catch (error) {
      setError('Maintenance create failed', error)
    }
  }

  const handleUpdateMaintenance = async () => {
    try {
      const data = await apiRequest(`/maintenance-requests/${maintenanceUpdate.id}`, {
        method: 'PUT',
        body: JSON.stringify(buildMaintenancePayload(maintenanceUpdate)),
      })
      setResult('Maintenance request updated', data)
      setActiveModal(null)
    } catch (error) {
      setError('Maintenance update failed', error)
    }
  }

  const handleDeleteMaintenance = async () => {
    try {
      const data = await apiRequest(`/maintenance-requests/${maintenanceId}`, { method: 'DELETE' })
      setResult('Maintenance request deleted', data)
    } catch (error) {
      setError('Maintenance delete failed', error)
    }
  }

  const handleGetMaintenanceById = async () => {
    try {
      const data = await apiRequest(`/maintenance-requests/${maintenanceId}`)
      setMaintenanceList(data ? [data] : [])
      setResult('Maintenance request by ID', data)
    } catch (error) {
      setError('Maintenance fetch failed', error)
    }
  }

  const handleGetAllMaintenance = async () => {
    try {
      const data = await apiRequest('/maintenance-requests')
      setMaintenanceList(Array.isArray(data) ? data : [])
      setResult('All maintenance requests', data)
    } catch (error) {
      setError('Maintenance fetch failed', error)
    }
  }

  const handleGetMaintenanceByProperty = async () => {
    try {
      const data = await apiRequest(`/maintenance-requests/property/${maintenancePropertyId}`)
      setMaintenanceList(Array.isArray(data) ? data : [])
      setResult('Maintenance by property', data)
    } catch (error) {
      setError('Maintenance by property failed', error)
    }
  }

  const handleGetMaintenanceByStatus = async () => {
    try {
      const data = await apiRequest(`/maintenance-requests/status/${maintenanceStatus}`)
      setMaintenanceList(Array.isArray(data) ? data : [])
      setResult('Maintenance by status', data)
    } catch (error) {
      setError('Maintenance by status failed', error)
    }
  }

  const handleGetMaintenanceByPriority = async () => {
    try {
      const data = await apiRequest(`/maintenance-requests/priority/${maintenancePriority}`)
      setMaintenanceList(Array.isArray(data) ? data : [])
      setResult('Maintenance by priority', data)
    } catch (error) {
      setError('Maintenance by priority failed', error)
    }
  }

  const handleGetPendingMaintenance = async () => {
    try {
      const data = await apiRequest('/maintenance-requests/pending')
      setMaintenanceList(Array.isArray(data) ? data : [])
      setResult('Pending maintenance', data)
    } catch (error) {
      setError('Pending maintenance failed', error)
    }
  }

  const handleGetUrgentMaintenance = async () => {
    try {
      const data = await apiRequest('/maintenance-requests/urgent')
      setMaintenanceList(Array.isArray(data) ? data : [])
      setResult('Urgent maintenance', data)
    } catch (error) {
      setError('Urgent maintenance failed', error)
    }
  }

  const handleEditProperty = (property) => {
    setPropertyUpdate({
      id: toInput(property.propertyId),
      address: toInput(property.address),
      city: toInput(property.city),
      state: toInput(property.state),
      zipCode: toInput(property.zipCode),
      propertyType: toInput(property.propertyType || 'APARTMENT'),
      bedrooms: toInput(property.bedrooms),
      bathrooms: toInput(property.bathrooms),
      squareFeet: toInput(property.squareFeet),
      monthlyRent: toInput(property.monthlyRent),
      purchaseDate: toInput(property.purchaseDate),
      purchasePrice: toInput(property.purchasePrice),
    })
    setActiveModal('property')
  }

  const handleDeletePropertyFromList = async (id) => {
    try {
      await apiRequest(`/properties/${id}`, { method: 'DELETE' })
      setPropertiesList((prev) => prev.filter((item) => item.propertyId !== id))
      setResult('Property deleted', { id })
    } catch (error) {
      setError('Property delete failed', error)
    }
  }

  const handleEditTenant = (tenant) => {
    setTenantUpdate({
      id: toInput(tenant.tenantId),
      firstName: toInput(tenant.firstName),
      lastName: toInput(tenant.lastName),
      email: toInput(tenant.email),
      phone: toInput(tenant.phone),
      dateOfBirth: toInput(tenant.dateOfBirth),
      ssnLastFour: toInput(tenant.ssnLastFour),
      employmentStatus: toInput(tenant.employmentStatus || 'EMPLOYED'),
      monthlyIncome: toInput(tenant.monthlyIncome),
      emergencyContactName: toInput(tenant.emergencyContactName),
      emergencyContactPhone: toInput(tenant.emergencyContactPhone),
    })
    setActiveModal('tenant')
  }

  const handleDeleteTenantFromList = async (id) => {
    try {
      await apiRequest(`/tenants/${id}`, { method: 'DELETE' })
      setTenantsList((prev) => prev.filter((item) => item.tenantId !== id))
      setResult('Tenant deleted', { id })
    } catch (error) {
      setError('Tenant delete failed', error)
    }
  }

  const handleEditLease = (lease) => {
    setLeaseUpdate({
      id: toInput(lease.leaseId),
      propertyId: toInput(lease.propertyId),
      tenantId: toInput(lease.tenantId),
      startDate: toInput(lease.startDate),
      endDate: toInput(lease.endDate),
      monthlyRent: toInput(lease.monthlyRent),
      securityDeposit: toInput(lease.securityDeposit),
      leaseStatus: toInput(lease.leaseStatus || 'PENDING'),
      leaseTerms: toInput(lease.leaseTerms),
    })
    setActiveModal('lease')
  }

  const handleDeleteLeaseFromList = async (id) => {
    try {
      await apiRequest(`/leases/${id}`, { method: 'DELETE' })
      setLeasesList((prev) => prev.filter((item) => item.leaseId !== id))
      setResult('Lease deleted', { id })
    } catch (error) {
      setError('Lease delete failed', error)
    }
  }

  const handleEditPayment = (payment) => {
    setPaymentUpdate({
      id: toInput(payment.paymentId),
      leaseId: toInput(payment.leaseId),
      paymentDate: toInput(payment.paymentDate),
      amount: toInput(payment.amount),
      paymentType: toInput(payment.paymentType || 'RENT'),
      paymentMethod: toInput(payment.paymentMethod || 'ONLINE'),
      paymentStatus: toInput(payment.paymentStatus || 'PENDING'),
      dueDate: toInput(payment.dueDate),
      notes: toInput(payment.notes),
    })
    setActiveModal('payment')
  }

  const handleDeletePaymentFromList = async (id) => {
    try {
      await apiRequest(`/payments/${id}`, { method: 'DELETE' })
      setPaymentsList((prev) => prev.filter((item) => item.paymentId !== id))
      setResult('Payment deleted', { id })
    } catch (error) {
      setError('Payment delete failed', error)
    }
  }

  const handleEditMaintenance = (request) => {
    setMaintenanceUpdate({
      id: toInput(request.requestId),
      propertyId: toInput(request.propertyId),
      tenantId: toInput(request.tenantId),
      requestDate: toInput(request.requestDate),
      description: toInput(request.description),
      priority: toInput(request.priority || 'MEDIUM'),
      status: toInput(request.status || 'OPEN'),
      estimatedCost: toInput(request.estimatedCost),
      actualCost: toInput(request.actualCost),
      completionDate: toInput(request.completionDate),
      notes: toInput(request.notes),
    })
    setActiveModal('maintenance')
  }

  const handleDeleteMaintenanceFromList = async (id) => {
    try {
      await apiRequest(`/maintenance-requests/${id}`, { method: 'DELETE' })
      setMaintenanceList((prev) => prev.filter((item) => item.requestId !== id))
      setResult('Maintenance request deleted', { id })
    } catch (error) {
      setError('Maintenance delete failed', error)
    }
  }

  return (
    <div className="app">
      <header className="app-header">
        <div className="brand">
          <img src="/rentflowlogo.png" alt="RentFlow" className="brand-logo" />
        </div>
        <nav className="nav">
          <button className={activePage === 'dashboard' ? 'nav-active' : ''} onClick={() => setActivePage('dashboard')}>Dashboard</button>
          <button className={activePage === 'properties' ? 'nav-active' : ''} onClick={() => setActivePage('properties')}>Properties</button>
          <button className={activePage === 'tenants' ? 'nav-active' : ''} onClick={() => setActivePage('tenants')}>Tenants</button>
          <button className={activePage === 'leases' ? 'nav-active' : ''} onClick={() => setActivePage('leases')}>Leases</button>
          <button className={activePage === 'payments' ? 'nav-active' : ''} onClick={() => setActivePage('payments')}>Payments</button>
          <button className={activePage === 'maintenance' ? 'nav-active' : ''} onClick={() => setActivePage('maintenance')}>Maintenance</button>
          <button className={activePage === 'output' ? 'nav-active' : ''} onClick={() => setActivePage('output')}>Last Response</button>
        </nav>
      </header>

      {activePage === 'dashboard' && (
        <section className="panel">
          <h2>Dashboard</h2>
          <p className="subtitle">Quick access to full lists. Click any button to load and view the data.</p>
          <div className="dashboard-grid">
            <button onClick={handleGetAllProperties}>Load All Properties</button>
            <button onClick={handleGetAllTenants}>Load All Tenants</button>
            <button onClick={handleGetAllLeases}>Load All Leases</button>
            <button onClick={handleGetAllPayments}>Load All Payments</button>
            <button onClick={handleGetAllMaintenance}>Load All Maintenance</button>
          </div>
          <div className="dashboard-cards">
            <div className="metric">
              <span>Properties</span>
              <strong>{propertiesList.length}</strong>
            </div>
            <div className="metric">
              <span>Tenants</span>
              <strong>{tenantsList.length}</strong>
            </div>
            <div className="metric">
              <span>Leases</span>
              <strong>{leasesList.length}</strong>
            </div>
            <div className="metric">
              <span>Payments</span>
              <strong>{paymentsList.length}</strong>
            </div>
            <div className="metric">
              <span>Maintenance</span>
              <strong>{maintenanceList.length}</strong>
            </div>
          </div>
        </section>
      )}

      {activePage === 'properties' && (
      <section className="panel">
        <h2>Properties</h2>
        <div className="panel-grid">
          <div className="card">
            <h3>Create Property</h3>
            <div className="form-grid">
              <input placeholder="Address" value={propertyCreate.address} onChange={(e) => setPropertyCreate({ ...propertyCreate, address: e.target.value })} />
              <input placeholder="City" value={propertyCreate.city} onChange={(e) => setPropertyCreate({ ...propertyCreate, city: e.target.value })} />
              <input placeholder="State" value={propertyCreate.state} onChange={(e) => setPropertyCreate({ ...propertyCreate, state: e.target.value })} />
              <input placeholder="Zip Code" value={propertyCreate.zipCode} onChange={(e) => setPropertyCreate({ ...propertyCreate, zipCode: e.target.value })} />
              <select value={propertyCreate.propertyType} onChange={(e) => setPropertyCreate({ ...propertyCreate, propertyType: e.target.value })}>
                {PROPERTY_TYPES.map((type) => (
                  <option key={type} value={type}>{type}</option>
                ))}
              </select>
              <input type="number" placeholder="Bedrooms" value={propertyCreate.bedrooms} onChange={(e) => setPropertyCreate({ ...propertyCreate, bedrooms: e.target.value })} />
              <input type="number" step="0.5" placeholder="Bathrooms" value={propertyCreate.bathrooms} onChange={(e) => setPropertyCreate({ ...propertyCreate, bathrooms: e.target.value })} />
              <input type="number" placeholder="Square Feet" value={propertyCreate.squareFeet} onChange={(e) => setPropertyCreate({ ...propertyCreate, squareFeet: e.target.value })} />
              <input type="number" step="0.01" placeholder="Monthly Rent" value={propertyCreate.monthlyRent} onChange={(e) => setPropertyCreate({ ...propertyCreate, monthlyRent: e.target.value })} />
              <input type="date" placeholder="Purchase Date" value={propertyCreate.purchaseDate} onChange={(e) => setPropertyCreate({ ...propertyCreate, purchaseDate: e.target.value })} />
              <input type="number" step="0.01" placeholder="Purchase Price" value={propertyCreate.purchasePrice} onChange={(e) => setPropertyCreate({ ...propertyCreate, purchasePrice: e.target.value })} />
            </div>
            <button onClick={handleCreateProperty}>Create Property</button>
          </div>

          {activeModal === 'property' && (
            <div className="modal-overlay" onClick={() => setActiveModal(null)}>
              <div className="modal" onClick={(event) => event.stopPropagation()}>
                <div className="modal-header">
                  <h3>Update Property</h3>
                  <button className="ghost" onClick={() => setActiveModal(null)}>Close</button>
                </div>
                <div className="form-grid">
                  <input placeholder="Property ID" value={propertyUpdate.id} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, id: e.target.value })} />
                  <input placeholder="Address" value={propertyUpdate.address} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, address: e.target.value })} />
                  <input placeholder="City" value={propertyUpdate.city} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, city: e.target.value })} />
                  <input placeholder="State" value={propertyUpdate.state} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, state: e.target.value })} />
                  <input placeholder="Zip Code" value={propertyUpdate.zipCode} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, zipCode: e.target.value })} />
                  <select value={propertyUpdate.propertyType} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, propertyType: e.target.value })}>
                    {PROPERTY_TYPES.map((type) => (
                      <option key={type} value={type}>{type}</option>
                    ))}
                  </select>
                  <input type="number" placeholder="Bedrooms" value={propertyUpdate.bedrooms} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, bedrooms: e.target.value })} />
                  <input type="number" step="0.5" placeholder="Bathrooms" value={propertyUpdate.bathrooms} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, bathrooms: e.target.value })} />
                  <input type="number" placeholder="Square Feet" value={propertyUpdate.squareFeet} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, squareFeet: e.target.value })} />
                  <input type="number" step="0.01" placeholder="Monthly Rent" value={propertyUpdate.monthlyRent} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, monthlyRent: e.target.value })} />
                  <input type="date" placeholder="Purchase Date" value={propertyUpdate.purchaseDate} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, purchaseDate: e.target.value })} />
                  <input type="number" step="0.01" placeholder="Purchase Price" value={propertyUpdate.purchasePrice} onChange={(e) => setPropertyUpdate({ ...propertyUpdate, purchasePrice: e.target.value })} />
                </div>
                <div className="modal-actions">
                  <button onClick={handleUpdateProperty}>Update Property</button>
                </div>
              </div>
            </div>
          )}

          <div className="card">
            <h3>Property Reads & Filters</h3>
            <div className="stack">
              <div className="row">
                <input placeholder="Property ID" value={propertyId} onChange={(e) => setPropertyId(e.target.value)} />
                <button onClick={handleGetPropertyById}>Get by ID</button>
                <button onClick={handleDeleteProperty} className="danger">Delete</button>
              </div>
              <button onClick={handleGetAllProperties}>Get All Properties</button>
              <div className="row">
                <select value={propertyType} onChange={(e) => setPropertyType(e.target.value)}>
                  {PROPERTY_TYPES.map((type) => (
                    <option key={type} value={type}>{type}</option>
                  ))}
                </select>
                <button onClick={handleGetPropertiesByType}>Get by Type</button>
              </div>
              <div className="row">
                <input type="number" placeholder="Min Rent" value={propertyRentRange.min} onChange={(e) => setPropertyRentRange({ ...propertyRentRange, min: e.target.value })} />
                <input type="number" placeholder="Max Rent" value={propertyRentRange.max} onChange={(e) => setPropertyRentRange({ ...propertyRentRange, max: e.target.value })} />
                <button onClick={handleGetPropertiesByRentRange}>Get by Rent Range</button>
              </div>
            </div>
          </div>
        </div>
        <div className="list-panel">
          <h3>Properties List</h3>
          <div className="list-grid">
            {propertiesList.length === 0 && <p className="empty">No properties loaded yet.</p>}
            {propertiesList.map((property) => (
              <div key={property.propertyId} className="list-card">
                <h4>{property.address}</h4>
                <p>{property.city}, {property.state} {property.zipCode}</p>
                <p>Type: {property.propertyType}</p>
                <p>{property.bedrooms} bd • {property.bathrooms} ba</p>
                <p>Rent: ${property.monthlyRent}</p>
                <div className="list-actions">
                  <button onClick={() => handleEditProperty(property)}>Edit</button>
                  <button className="danger" onClick={() => handleDeletePropertyFromList(property.propertyId)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
      )}

      {activePage === 'tenants' && (
      <section className="panel">
        <h2>Tenants</h2>
        <div className="panel-grid">
          <div className="card">
            <h3>Create Tenant</h3>
            <div className="form-grid">
              <input placeholder="First Name" value={tenantCreate.firstName} onChange={(e) => setTenantCreate({ ...tenantCreate, firstName: e.target.value })} />
              <input placeholder="Last Name" value={tenantCreate.lastName} onChange={(e) => setTenantCreate({ ...tenantCreate, lastName: e.target.value })} />
              <input placeholder="Email" value={tenantCreate.email} onChange={(e) => setTenantCreate({ ...tenantCreate, email: e.target.value })} />
              <input placeholder="Phone" value={tenantCreate.phone} onChange={(e) => setTenantCreate({ ...tenantCreate, phone: e.target.value })} />
              <input type="date" placeholder="DOB" value={tenantCreate.dateOfBirth} onChange={(e) => setTenantCreate({ ...tenantCreate, dateOfBirth: e.target.value })} />
              <input placeholder="SSN Last 4" value={tenantCreate.ssnLastFour} onChange={(e) => setTenantCreate({ ...tenantCreate, ssnLastFour: e.target.value })} />
              <select value={tenantCreate.employmentStatus} onChange={(e) => setTenantCreate({ ...tenantCreate, employmentStatus: e.target.value })}>
                {EMPLOYMENT_STATUSES.map((status) => (
                  <option key={status} value={status}>{status}</option>
                ))}
              </select>
              <input type="number" step="0.01" placeholder="Monthly Income" value={tenantCreate.monthlyIncome} onChange={(e) => setTenantCreate({ ...tenantCreate, monthlyIncome: e.target.value })} />
              <input placeholder="Emergency Contact" value={tenantCreate.emergencyContactName} onChange={(e) => setTenantCreate({ ...tenantCreate, emergencyContactName: e.target.value })} />
              <input placeholder="Emergency Phone" value={tenantCreate.emergencyContactPhone} onChange={(e) => setTenantCreate({ ...tenantCreate, emergencyContactPhone: e.target.value })} />
            </div>
            <button onClick={handleCreateTenant}>Create Tenant</button>
          </div>

          {activeModal === 'tenant' && (
            <div className="modal-overlay" onClick={() => setActiveModal(null)}>
              <div className="modal" onClick={(event) => event.stopPropagation()}>
                <div className="modal-header">
                  <h3>Update Tenant</h3>
                  <button className="ghost" onClick={() => setActiveModal(null)}>Close</button>
                </div>
                <div className="form-grid">
                  <input placeholder="Tenant ID" value={tenantUpdate.id} onChange={(e) => setTenantUpdate({ ...tenantUpdate, id: e.target.value })} />
                  <input placeholder="First Name" value={tenantUpdate.firstName} onChange={(e) => setTenantUpdate({ ...tenantUpdate, firstName: e.target.value })} />
                  <input placeholder="Last Name" value={tenantUpdate.lastName} onChange={(e) => setTenantUpdate({ ...tenantUpdate, lastName: e.target.value })} />
                  <input placeholder="Email" value={tenantUpdate.email} onChange={(e) => setTenantUpdate({ ...tenantUpdate, email: e.target.value })} />
                  <input placeholder="Phone" value={tenantUpdate.phone} onChange={(e) => setTenantUpdate({ ...tenantUpdate, phone: e.target.value })} />
                  <input type="date" placeholder="DOB" value={tenantUpdate.dateOfBirth} onChange={(e) => setTenantUpdate({ ...tenantUpdate, dateOfBirth: e.target.value })} />
                  <input placeholder="SSN Last 4" value={tenantUpdate.ssnLastFour} onChange={(e) => setTenantUpdate({ ...tenantUpdate, ssnLastFour: e.target.value })} />
                  <select value={tenantUpdate.employmentStatus} onChange={(e) => setTenantUpdate({ ...tenantUpdate, employmentStatus: e.target.value })}>
                    {EMPLOYMENT_STATUSES.map((status) => (
                      <option key={status} value={status}>{status}</option>
                    ))}
                  </select>
                  <input type="number" step="0.01" placeholder="Monthly Income" value={tenantUpdate.monthlyIncome} onChange={(e) => setTenantUpdate({ ...tenantUpdate, monthlyIncome: e.target.value })} />
                  <input placeholder="Emergency Contact" value={tenantUpdate.emergencyContactName} onChange={(e) => setTenantUpdate({ ...tenantUpdate, emergencyContactName: e.target.value })} />
                  <input placeholder="Emergency Phone" value={tenantUpdate.emergencyContactPhone} onChange={(e) => setTenantUpdate({ ...tenantUpdate, emergencyContactPhone: e.target.value })} />
                </div>
                <div className="modal-actions">
                  <button onClick={handleUpdateTenant}>Update Tenant</button>
                </div>
              </div>
            </div>
          )}

          <div className="card">
            <h3>Tenant Reads & Filters</h3>
            <div className="stack">
              <div className="row">
                <input placeholder="Tenant ID" value={tenantId} onChange={(e) => setTenantId(e.target.value)} />
                <button onClick={handleGetTenantById}>Get by ID</button>
                <button onClick={handleDeleteTenant} className="danger">Delete</button>
              </div>
              <button onClick={handleGetAllTenants}>Get All Tenants</button>
              <div className="row">
                <input placeholder="Email" value={tenantEmail} onChange={(e) => setTenantEmail(e.target.value)} />
                <button onClick={handleGetTenantByEmail}>Get by Email</button>
              </div>
              <div className="row">
                <input placeholder="Phone" value={tenantPhone} onChange={(e) => setTenantPhone(e.target.value)} />
                <button onClick={handleGetTenantByPhone}>Get by Phone</button>
              </div>
              <div className="row">
                <select value={tenantEmployment} onChange={(e) => setTenantEmployment(e.target.value)}>
                  {EMPLOYMENT_STATUSES.map((status) => (
                    <option key={status} value={status}>{status}</option>
                  ))}
                </select>
                <button onClick={handleGetTenantsByEmployment}>Get by Employment</button>
              </div>
              <div className="row">
                <input placeholder="Search Name" value={tenantSearch} onChange={(e) => setTenantSearch(e.target.value)} />
                <button onClick={handleSearchTenants}>Search</button>
              </div>
            </div>
          </div>
        </div>
        <div className="list-panel">
          <h3>Tenants List</h3>
          <div className="list-grid">
            {tenantsList.length === 0 && <p className="empty">No tenants loaded yet.</p>}
            {tenantsList.map((tenant) => (
              <div key={tenant.tenantId} className="list-card">
                <h4>{tenant.firstName} {tenant.lastName}</h4>
                <p>{tenant.email}</p>
                <p>{tenant.phone}</p>
                <p>Status: {tenant.employmentStatus}</p>
                {tenant.monthlyIncome && <p>Income: ${tenant.monthlyIncome}</p>}
                <div className="list-actions">
                  <button onClick={() => handleEditTenant(tenant)}>Edit</button>
                  <button className="danger" onClick={() => handleDeleteTenantFromList(tenant.tenantId)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
      )}

      {activePage === 'leases' && (
      <section className="panel">
        <h2>Leases</h2>
        <div className="panel-grid">
          <div className="card">
            <h3>Create Lease</h3>
            <div className="form-grid">
              <input placeholder="Property ID" value={leaseCreate.propertyId} onChange={(e) => setLeaseCreate({ ...leaseCreate, propertyId: e.target.value })} />
              <input placeholder="Tenant ID" value={leaseCreate.tenantId} onChange={(e) => setLeaseCreate({ ...leaseCreate, tenantId: e.target.value })} />
              <input type="date" placeholder="Start Date" value={leaseCreate.startDate} onChange={(e) => setLeaseCreate({ ...leaseCreate, startDate: e.target.value })} />
              <input type="date" placeholder="End Date" value={leaseCreate.endDate} onChange={(e) => setLeaseCreate({ ...leaseCreate, endDate: e.target.value })} />
              <input type="number" step="0.01" placeholder="Monthly Rent" value={leaseCreate.monthlyRent} onChange={(e) => setLeaseCreate({ ...leaseCreate, monthlyRent: e.target.value })} />
              <input type="number" step="0.01" placeholder="Security Deposit" value={leaseCreate.securityDeposit} onChange={(e) => setLeaseCreate({ ...leaseCreate, securityDeposit: e.target.value })} />
              <select value={leaseCreate.leaseStatus} onChange={(e) => setLeaseCreate({ ...leaseCreate, leaseStatus: e.target.value })}>
                {LEASE_STATUSES.map((status) => (
                  <option key={status} value={status}>{status}</option>
                ))}
              </select>
              <input placeholder="Lease Terms" value={leaseCreate.leaseTerms} onChange={(e) => setLeaseCreate({ ...leaseCreate, leaseTerms: e.target.value })} />
            </div>
            <button onClick={handleCreateLease}>Create Lease</button>
          </div>

          {activeModal === 'lease' && (
            <div className="modal-overlay" onClick={() => setActiveModal(null)}>
              <div className="modal" onClick={(event) => event.stopPropagation()}>
                <div className="modal-header">
                  <h3>Update Lease</h3>
                  <button className="ghost" onClick={() => setActiveModal(null)}>Close</button>
                </div>
                <div className="form-grid">
                  <input placeholder="Lease ID" value={leaseUpdate.id} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, id: e.target.value })} />
                  <input placeholder="Property ID" value={leaseUpdate.propertyId} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, propertyId: e.target.value })} />
                  <input placeholder="Tenant ID" value={leaseUpdate.tenantId} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, tenantId: e.target.value })} />
                  <input type="date" placeholder="Start Date" value={leaseUpdate.startDate} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, startDate: e.target.value })} />
                  <input type="date" placeholder="End Date" value={leaseUpdate.endDate} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, endDate: e.target.value })} />
                  <input type="number" step="0.01" placeholder="Monthly Rent" value={leaseUpdate.monthlyRent} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, monthlyRent: e.target.value })} />
                  <input type="number" step="0.01" placeholder="Security Deposit" value={leaseUpdate.securityDeposit} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, securityDeposit: e.target.value })} />
                  <select value={leaseUpdate.leaseStatus} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, leaseStatus: e.target.value })}>
                    {LEASE_STATUSES.map((status) => (
                      <option key={status} value={status}>{status}</option>
                    ))}
                  </select>
                  <input placeholder="Lease Terms" value={leaseUpdate.leaseTerms} onChange={(e) => setLeaseUpdate({ ...leaseUpdate, leaseTerms: e.target.value })} />
                </div>
                <div className="modal-actions">
                  <button onClick={handleUpdateLease}>Update Lease</button>
                </div>
              </div>
            </div>
          )}

          <div className="card">
            <h3>Lease Reads & Filters</h3>
            <div className="stack">
              <div className="row">
                <input placeholder="Lease ID" value={leaseId} onChange={(e) => setLeaseId(e.target.value)} />
                <button onClick={handleGetLeaseById}>Get by ID</button>
                <button onClick={handleDeleteLease} className="danger">Delete</button>
              </div>
              <button onClick={handleGetAllLeases}>Get All Leases</button>
              <div className="row">
                <input placeholder="Property ID" value={leasePropertyId} onChange={(e) => setLeasePropertyId(e.target.value)} />
                <button onClick={handleGetLeasesByProperty}>Get by Property</button>
              </div>
              <div className="row">
                <input placeholder="Tenant ID" value={leaseTenantId} onChange={(e) => setLeaseTenantId(e.target.value)} />
                <button onClick={handleGetLeasesByTenant}>Get by Tenant</button>
              </div>
              <div className="row">
                <select value={leaseStatus} onChange={(e) => setLeaseStatus(e.target.value)}>
                  {LEASE_STATUSES.map((status) => (
                    <option key={status} value={status}>{status}</option>
                  ))}
                </select>
                <button onClick={handleGetLeasesByStatus}>Get by Status</button>
              </div>
              <div className="row">
                <button onClick={handleGetActiveLeases}>Get Active Leases</button>
              </div>
              <div className="row">
                <input placeholder="Days" value={leaseExpiringDays} onChange={(e) => setLeaseExpiringDays(e.target.value)} />
                <button onClick={handleGetLeasesExpiringSoon}>Expiring Soon</button>
              </div>
            </div>
          </div>
        </div>
        <div className="list-panel">
          <h3>Leases List</h3>
          <div className="list-grid">
            {leasesList.length === 0 && <p className="empty">No leases loaded yet.</p>}
            {leasesList.map((lease) => (
              <div key={lease.leaseId} className="list-card">
                <h4>Lease #{lease.leaseId}</h4>
                <p>Property: {lease.propertyAddress ?? `ID ${lease.propertyId ?? 'N/A'}`}</p>
                <p>Tenant: {lease.tenantName ?? `ID ${lease.tenantId ?? 'N/A'}`}</p>
                <p>{lease.startDate} → {lease.endDate}</p>
                <p>Status: {lease.leaseStatus}</p>
                <p>Rent: ${lease.monthlyRent}</p>
                <div className="list-actions">
                  <button onClick={() => handleEditLease(lease)}>Edit</button>
                  <button className="danger" onClick={() => handleDeleteLeaseFromList(lease.leaseId)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
      )}

      {activePage === 'payments' && (
      <section className="panel">
        <h2>Payments</h2>
        <div className="panel-grid">
          <div className="card">
            <h3>Create Payment</h3>
            <div className="form-grid">
              <input placeholder="Lease ID" value={paymentCreate.leaseId} onChange={(e) => setPaymentCreate({ ...paymentCreate, leaseId: e.target.value })} />
              <input type="date" placeholder="Payment Date" value={paymentCreate.paymentDate} onChange={(e) => setPaymentCreate({ ...paymentCreate, paymentDate: e.target.value })} />
              <input type="number" step="0.01" placeholder="Amount" value={paymentCreate.amount} onChange={(e) => setPaymentCreate({ ...paymentCreate, amount: e.target.value })} />
              <select value={paymentCreate.paymentType} onChange={(e) => setPaymentCreate({ ...paymentCreate, paymentType: e.target.value })}>
                {PAYMENT_TYPES.map((type) => (
                  <option key={type} value={type}>{type}</option>
                ))}
              </select>
              <select value={paymentCreate.paymentMethod} onChange={(e) => setPaymentCreate({ ...paymentCreate, paymentMethod: e.target.value })}>
                {PAYMENT_METHODS.map((method) => (
                  <option key={method} value={method}>{method}</option>
                ))}
              </select>
              <select value={paymentCreate.paymentStatus} onChange={(e) => setPaymentCreate({ ...paymentCreate, paymentStatus: e.target.value })}>
                {PAYMENT_STATUSES.map((status) => (
                  <option key={status} value={status}>{status}</option>
                ))}
              </select>
              <input type="date" placeholder="Due Date" value={paymentCreate.dueDate} onChange={(e) => setPaymentCreate({ ...paymentCreate, dueDate: e.target.value })} />
              <input placeholder="Notes" value={paymentCreate.notes} onChange={(e) => setPaymentCreate({ ...paymentCreate, notes: e.target.value })} />
            </div>
            <button onClick={handleCreatePayment}>Create Payment</button>
          </div>

          {activeModal === 'payment' && (
            <div className="modal-overlay" onClick={() => setActiveModal(null)}>
              <div className="modal" onClick={(event) => event.stopPropagation()}>
                <div className="modal-header">
                  <h3>Update Payment</h3>
                  <button className="ghost" onClick={() => setActiveModal(null)}>Close</button>
                </div>
                <div className="form-grid">
                  <input placeholder="Payment ID" value={paymentUpdate.id} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, id: e.target.value })} />
                  <input placeholder="Lease ID" value={paymentUpdate.leaseId} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, leaseId: e.target.value })} />
                  <input type="date" placeholder="Payment Date" value={paymentUpdate.paymentDate} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, paymentDate: e.target.value })} />
                  <input type="number" step="0.01" placeholder="Amount" value={paymentUpdate.amount} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, amount: e.target.value })} />
                  <select value={paymentUpdate.paymentType} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, paymentType: e.target.value })}>
                    {PAYMENT_TYPES.map((type) => (
                      <option key={type} value={type}>{type}</option>
                    ))}
                  </select>
                  <select value={paymentUpdate.paymentMethod} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, paymentMethod: e.target.value })}>
                    {PAYMENT_METHODS.map((method) => (
                      <option key={method} value={method}>{method}</option>
                    ))}
                  </select>
                  <select value={paymentUpdate.paymentStatus} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, paymentStatus: e.target.value })}>
                    {PAYMENT_STATUSES.map((status) => (
                      <option key={status} value={status}>{status}</option>
                    ))}
                  </select>
                  <input type="date" placeholder="Due Date" value={paymentUpdate.dueDate} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, dueDate: e.target.value })} />
                  <input placeholder="Notes" value={paymentUpdate.notes} onChange={(e) => setPaymentUpdate({ ...paymentUpdate, notes: e.target.value })} />
                </div>
                <div className="modal-actions">
                  <button onClick={handleUpdatePayment}>Update Payment</button>
                </div>
              </div>
            </div>
          )}

          <div className="card">
            <h3>Payment Reads & Filters</h3>
            <div className="stack">
              <div className="row">
                <input placeholder="Payment ID" value={paymentId} onChange={(e) => setPaymentId(e.target.value)} />
                <button onClick={handleGetPaymentById}>Get by ID</button>
                <button onClick={handleDeletePayment} className="danger">Delete</button>
              </div>
              <button onClick={handleGetAllPayments}>Get All Payments</button>
              <div className="row">
                <input placeholder="Lease ID" value={paymentLeaseId} onChange={(e) => setPaymentLeaseId(e.target.value)} />
                <button onClick={handleGetPaymentsByLease}>Get by Lease</button>
              </div>
              <div className="row">
                <select value={paymentStatus} onChange={(e) => setPaymentStatus(e.target.value)}>
                  {PAYMENT_STATUSES.map((status) => (
                    <option key={status} value={status}>{status}</option>
                  ))}
                </select>
                <button onClick={handleGetPaymentsByStatus}>Get by Status</button>
              </div>
              <button onClick={handleGetOverduePayments}>Get Overdue Payments</button>
              <div className="row">
                <input type="date" placeholder="Start Date" value={paymentDateRange.startDate} onChange={(e) => setPaymentDateRange({ ...paymentDateRange, startDate: e.target.value })} />
                <input type="date" placeholder="End Date" value={paymentDateRange.endDate} onChange={(e) => setPaymentDateRange({ ...paymentDateRange, endDate: e.target.value })} />
                <button onClick={handleGetPaymentsByDateRange}>Get by Date Range</button>
              </div>
              <div className="row">
                <input placeholder="Lease ID" value={paymentTotalLeaseId} onChange={(e) => setPaymentTotalLeaseId(e.target.value)} />
                <button onClick={handleGetPaymentsTotalForLease}>Lease Total</button>
              </div>
            </div>
          </div>
        </div>
        <div className="list-panel">
          <h3>Payments List</h3>
          <div className="list-grid">
            {paymentsList.length === 0 && <p className="empty">No payments loaded yet.</p>}
            {paymentsList.map((payment) => (
              <div key={payment.paymentId} className="list-card">
                <h4>Payment #{payment.paymentId}</h4>
                <p>Tenant: {payment.tenantName ?? 'N/A'}</p>
                <p>Property: {payment.propertyAddress ?? `Lease ID ${payment.leaseId ?? 'N/A'}`}</p>
                <p>Amount: ${payment.amount}</p>
                <p>Type: {payment.paymentType}</p>
                <p>Status: {payment.paymentStatus}</p>
                <p>Date: {payment.paymentDate}</p>
                <div className="list-actions">
                  <button onClick={() => handleEditPayment(payment)}>Edit</button>
                  <button className="danger" onClick={() => handleDeletePaymentFromList(payment.paymentId)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
      )}

      {activePage === 'maintenance' && (
      <section className="panel">
        <h2>Maintenance Requests</h2>
        <div className="panel-grid">
          <div className="card">
            <h3>Create Maintenance Request</h3>
            <div className="form-grid">
              <input placeholder="Property ID" value={maintenanceCreate.propertyId} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, propertyId: e.target.value })} />
              <input placeholder="Tenant ID (optional)" value={maintenanceCreate.tenantId} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, tenantId: e.target.value })} />
              <input type="date" placeholder="Request Date" value={maintenanceCreate.requestDate} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, requestDate: e.target.value })} />
              <input placeholder="Description" value={maintenanceCreate.description} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, description: e.target.value })} />
              <select value={maintenanceCreate.priority} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, priority: e.target.value })}>
                {PRIORITIES.map((priority) => (
                  <option key={priority} value={priority}>{priority}</option>
                ))}
              </select>
              <select value={maintenanceCreate.status} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, status: e.target.value })}>
                {MAINTENANCE_STATUSES.map((status) => (
                  <option key={status} value={status}>{status}</option>
                ))}
              </select>
              <input type="number" step="0.01" placeholder="Estimated Cost" value={maintenanceCreate.estimatedCost} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, estimatedCost: e.target.value })} />
              <input type="number" step="0.01" placeholder="Actual Cost" value={maintenanceCreate.actualCost} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, actualCost: e.target.value })} />
              <input type="date" placeholder="Completion Date" value={maintenanceCreate.completionDate} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, completionDate: e.target.value })} />
              <input placeholder="Notes" value={maintenanceCreate.notes} onChange={(e) => setMaintenanceCreate({ ...maintenanceCreate, notes: e.target.value })} />
            </div>
            <button onClick={handleCreateMaintenance}>Create Maintenance Request</button>
          </div>

          {activeModal === 'maintenance' && (
            <div className="modal-overlay" onClick={() => setActiveModal(null)}>
              <div className="modal" onClick={(event) => event.stopPropagation()}>
                <div className="modal-header">
                  <h3>Update Maintenance Request</h3>
                  <button className="ghost" onClick={() => setActiveModal(null)}>Close</button>
                </div>
                <div className="form-grid">
                  <input placeholder="Request ID" value={maintenanceUpdate.id} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, id: e.target.value })} />
                  <input placeholder="Property ID" value={maintenanceUpdate.propertyId} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, propertyId: e.target.value })} />
                  <input placeholder="Tenant ID (optional)" value={maintenanceUpdate.tenantId} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, tenantId: e.target.value })} />
                  <input type="date" placeholder="Request Date" value={maintenanceUpdate.requestDate} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, requestDate: e.target.value })} />
                  <input placeholder="Description" value={maintenanceUpdate.description} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, description: e.target.value })} />
                  <select value={maintenanceUpdate.priority} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, priority: e.target.value })}>
                    {PRIORITIES.map((priority) => (
                      <option key={priority} value={priority}>{priority}</option>
                    ))}
                  </select>
                  <select value={maintenanceUpdate.status} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, status: e.target.value })}>
                    {MAINTENANCE_STATUSES.map((status) => (
                      <option key={status} value={status}>{status}</option>
                    ))}
                  </select>
                  <input type="number" step="0.01" placeholder="Estimated Cost" value={maintenanceUpdate.estimatedCost} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, estimatedCost: e.target.value })} />
                  <input type="number" step="0.01" placeholder="Actual Cost" value={maintenanceUpdate.actualCost} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, actualCost: e.target.value })} />
                  <input type="date" placeholder="Completion Date" value={maintenanceUpdate.completionDate} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, completionDate: e.target.value })} />
                  <input placeholder="Notes" value={maintenanceUpdate.notes} onChange={(e) => setMaintenanceUpdate({ ...maintenanceUpdate, notes: e.target.value })} />
                </div>
                <div className="modal-actions">
                  <button onClick={handleUpdateMaintenance}>Update Maintenance Request</button>
                </div>
              </div>
            </div>
          )}

          <div className="card">
            <h3>Maintenance Reads & Filters</h3>
            <div className="stack">
              <div className="row">
                <input placeholder="Request ID" value={maintenanceId} onChange={(e) => setMaintenanceId(e.target.value)} />
                <button onClick={handleGetMaintenanceById}>Get by ID</button>
                <button onClick={handleDeleteMaintenance} className="danger">Delete</button>
              </div>
              <button onClick={handleGetAllMaintenance}>Get All Requests</button>
              <div className="row">
                <input placeholder="Property ID" value={maintenancePropertyId} onChange={(e) => setMaintenancePropertyId(e.target.value)} />
                <button onClick={handleGetMaintenanceByProperty}>Get by Property</button>
              </div>
              <div className="row">
                <select value={maintenanceStatus} onChange={(e) => setMaintenanceStatus(e.target.value)}>
                  {MAINTENANCE_STATUSES.map((status) => (
                    <option key={status} value={status}>{status}</option>
                  ))}
                </select>
                <button onClick={handleGetMaintenanceByStatus}>Get by Status</button>
              </div>
              <div className="row">
                <select value={maintenancePriority} onChange={(e) => setMaintenancePriority(e.target.value)}>
                  {PRIORITIES.map((priority) => (
                    <option key={priority} value={priority}>{priority}</option>
                  ))}
                </select>
                <button onClick={handleGetMaintenanceByPriority}>Get by Priority</button>
              </div>
              <button onClick={handleGetPendingMaintenance}>Get Pending</button>
              <button onClick={handleGetUrgentMaintenance}>Get Urgent</button>
            </div>
          </div>
        </div>
        <div className="list-panel">
          <h3>Maintenance Requests List</h3>
          <div className="list-grid">
            {maintenanceList.length === 0 && <p className="empty">No maintenance requests loaded yet.</p>}
            {maintenanceList.map((request) => (
              <div key={request.requestId} className="list-card">
                <h4>Request #{request.requestId}</h4>
                <p>Property: {request.propertyAddress ?? `ID ${request.propertyId ?? 'N/A'}`}</p>
                <p>Tenant: {request.tenantName ?? `ID ${request.tenantId ?? 'N/A'}`}</p>
                <p>{request.description}</p>
                <p>Priority: {request.priority}</p>
                <p>Status: {request.status}</p>
                <div className="list-actions">
                  <button onClick={() => handleEditMaintenance(request)}>Edit</button>
                  <button className="danger" onClick={() => handleDeleteMaintenanceFromList(request.requestId)}>Delete</button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>
      )}

      {activePage === 'output' && (
      <section className="panel">
        <h2>Last Response</h2>
        <div className="output">
          <h4>{output.title}</h4>
          {output.error ? (
            <pre className="error">{output.error}</pre>
          ) : (
            <pre>{output.data ? JSON.stringify(output.data, null, 2) : 'No response yet.'}</pre>
          )}
        </div>
      </section>
      )}
    </div>
  )
}

export default App
