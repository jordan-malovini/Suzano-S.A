import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { 
  Search, 
  ClipboardList, 
  MapPin, 
  Calendar,
  Filter,
  Eye,
  Download,
  User
} from 'lucide-react'

const InspectionList = () => {
  const [inspections, setInspections] = useState([
    {
      id: 1,
      equipmentCode: 'EQ-001',
      equipmentName: 'Bomba Centrífuga A1',
      location: 'Área 1 - Setor A',
      inspector: 'João Silva',
      status: 'COMPLETED',
      result: 'APPROVED',
      scheduledDate: '2024-06-25 09:00',
      startedAt: '2024-06-25 09:15',
      completedAt: '2024-06-25 10:30',
      observations: 'Equipamento em boas condições',
      mediaCount: 5
    },
    {
      id: 2,
      equipmentCode: 'EQ-002',
      equipmentName: 'Motor Elétrico B2',
      location: 'Área 2 - Setor B',
      inspector: 'Maria Santos',
      status: 'IN_PROGRESS',
      scheduledDate: '2024-06-27 14:00',
      startedAt: '2024-06-27 14:10',
      mediaCount: 2
    },
    {
      id: 3,
      equipmentCode: 'EQ-003',
      equipmentName: 'Compressor C1',
      location: 'Área 1 - Setor C',
      inspector: 'Pedro Costa',
      status: 'PENDING',
      scheduledDate: '2024-06-27 16:00'
    },
    {
      id: 4,
      equipmentCode: 'EQ-004',
      equipmentName: 'Válvula de Controle D1',
      location: 'Área 3 - Setor D',
      inspector: 'Ana Oliveira',
      status: 'COMPLETED',
      result: 'REQUIRES_MAINTENANCE',
      scheduledDate: '2024-06-26 11:00',
      startedAt: '2024-06-26 11:05',
      completedAt: '2024-06-26 12:20',
      observations: 'Vazamento detectado na vedação',
      mediaCount: 8
    },
    {
      id: 5,
      equipmentCode: 'EQ-005',
      equipmentName: 'Tanque de Armazenamento E1',
      location: 'Área 2 - Setor E',
      inspector: 'Carlos Ferreira',
      status: 'NOT_PERFORMED',
      scheduledDate: '2024-06-26 15:00',
      pendingReason: 'Equipamento em operação'
    }
  ])

  const [filteredInspections, setFilteredInspections] = useState(inspections)
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState('ALL')
  const [resultFilter, setResultFilter] = useState('ALL')

  useEffect(() => {
    let filtered = inspections

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(inspection =>
        inspection.equipmentCode.toLowerCase().includes(searchTerm.toLowerCase()) ||
        inspection.equipmentName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        inspection.inspector.toLowerCase().includes(searchTerm.toLowerCase()) ||
        inspection.location.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    // Filter by status
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(inspection => inspection.status === statusFilter)
    }

    // Filter by result
    if (resultFilter !== 'ALL') {
      filtered = filtered.filter(inspection => inspection.result === resultFilter)
    }

    setFilteredInspections(filtered)
  }, [searchTerm, statusFilter, resultFilter, inspections])

  const getStatusBadge = (status) => {
    switch (status) {
      case 'PENDING':
        return <Badge className="bg-gray-100 text-gray-800">Pendente</Badge>
      case 'IN_PROGRESS':
        return <Badge className="bg-blue-100 text-blue-800">Em Andamento</Badge>
      case 'COMPLETED':
        return <Badge className="bg-green-100 text-green-800">Concluída</Badge>
      case 'NOT_PERFORMED':
        return <Badge className="bg-red-100 text-red-800">Não Realizada</Badge>
      default:
        return <Badge variant="secondary">{status}</Badge>
    }
  }

  const getResultBadge = (result) => {
    switch (result) {
      case 'APPROVED':
        return <Badge className="bg-green-100 text-green-800">Aprovado</Badge>
      case 'APPROVED_WITH_OBSERVATIONS':
        return <Badge className="bg-yellow-100 text-yellow-800">Aprovado c/ Observações</Badge>
      case 'REQUIRES_MAINTENANCE':
        return <Badge className="bg-orange-100 text-orange-800">Requer Manutenção</Badge>
      case 'CRITICAL':
        return <Badge className="bg-red-100 text-red-800">Crítico</Badge>
      default:
        return null
    }
  }

  const formatDate = (dateString) => {
    if (!dateString) return '-'
    const date = new Date(dateString)
    return date.toLocaleString('pt-BR')
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Inspeções</h1>
        <p className="text-gray-600">Gerencie e acompanhe as inspeções de equipamentos</p>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <Filter className="h-5 w-5" />
            <span>Filtros</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="text-sm font-medium mb-2 block">Buscar</label>
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                <Input
                  placeholder="Código, equipamento, inspetor..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
            <div>
              <label className="text-sm font-medium mb-2 block">Status</label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-md"
              >
                <option value="ALL">Todos</option>
                <option value="PENDING">Pendente</option>
                <option value="IN_PROGRESS">Em Andamento</option>
                <option value="COMPLETED">Concluída</option>
                <option value="NOT_PERFORMED">Não Realizada</option>
              </select>
            </div>
            <div>
              <label className="text-sm font-medium mb-2 block">Resultado</label>
              <select
                value={resultFilter}
                onChange={(e) => setResultFilter(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-md"
              >
                <option value="ALL">Todos</option>
                <option value="APPROVED">Aprovado</option>
                <option value="APPROVED_WITH_OBSERVATIONS">Aprovado c/ Observações</option>
                <option value="REQUIRES_MAINTENANCE">Requer Manutenção</option>
                <option value="CRITICAL">Crítico</option>
              </select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Inspections List */}
      <div className="space-y-4">
        {filteredInspections.map((inspection) => (
          <Card key={inspection.id} className="hover:shadow-lg transition-shadow">
            <CardContent className="p-6">
              <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between space-y-4 lg:space-y-0">
                <div className="flex-1">
                  <div className="flex items-center space-x-3 mb-2">
                    <h3 className="text-lg font-semibold">{inspection.equipmentCode}</h3>
                    {getStatusBadge(inspection.status)}
                    {inspection.result && getResultBadge(inspection.result)}
                  </div>
                  
                  <p className="text-gray-600 mb-2">{inspection.equipmentName}</p>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm text-gray-600">
                    <div className="flex items-center">
                      <MapPin className="h-4 w-4 mr-2" />
                      {inspection.location}
                    </div>
                    <div className="flex items-center">
                      <User className="h-4 w-4 mr-2" />
                      {inspection.inspector}
                    </div>
                    <div className="flex items-center">
                      <Calendar className="h-4 w-4 mr-2" />
                      Agendada: {formatDate(inspection.scheduledDate)}
                    </div>
                    {inspection.completedAt && (
                      <div className="flex items-center">
                        <Calendar className="h-4 w-4 mr-2" />
                        Concluída: {formatDate(inspection.completedAt)}
                      </div>
                    )}
                  </div>

                  {inspection.observations && (
                    <div className="mt-3 p-3 bg-gray-50 rounded-md">
                      <p className="text-sm text-gray-700">
                        <strong>Observações:</strong> {inspection.observations}
                      </p>
                    </div>
                  )}

                  {inspection.pendingReason && (
                    <div className="mt-3 p-3 bg-red-50 rounded-md">
                      <p className="text-sm text-red-700">
                        <strong>Motivo:</strong> {inspection.pendingReason}
                      </p>
                    </div>
                  )}

                  {inspection.mediaCount > 0 && (
                    <div className="mt-3">
                      <span className="text-sm text-gray-600">
                        {inspection.mediaCount} arquivo(s) de mídia anexado(s)
                      </span>
                    </div>
                  )}
                </div>

                <div className="flex flex-col space-y-2 lg:ml-6">
                  <Link to={`/inspection/${inspection.id}`}>
                    <Button variant="outline" size="sm" className="w-full">
                      <Eye className="h-4 w-4 mr-2" />
                      Visualizar
                    </Button>
                  </Link>
                  
                  {inspection.status === 'COMPLETED' && (
                    <Button variant="outline" size="sm" className="w-full">
                      <Download className="h-4 w-4 mr-2" />
                      Relatório
                    </Button>
                  )}
                  
                  {inspection.status === 'PENDING' && (
                    <Link to={`/inspection/new/${inspection.equipmentCode}`}>
                      <Button size="sm" className="w-full bg-green-600 hover:bg-green-700">
                        Iniciar
                      </Button>
                    </Link>
                  )}
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredInspections.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <ClipboardList className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Nenhuma inspeção encontrada</h3>
            <p className="text-gray-600">Tente ajustar os filtros ou verifique se há inspeções agendadas.</p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}

export default InspectionList

