import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Badge } from '@/components/ui/badge'
import { 
  Search, 
  Settings, 
  MapPin, 
  QrCode,
  Filter,
  Plus,
  Eye
} from 'lucide-react'

const EquipmentList = () => {
  const [equipments, setEquipments] = useState([
    {
      id: 1,
      code: 'EQ-001',
      name: 'Bomba Centrífuga A1',
      description: 'Bomba centrífuga para transferência de líquidos',
      location: 'Área 1 - Setor A',
      area: 'Área 1',
      sector: 'Setor A',
      type: 'PUMP',
      status: 'ACTIVE',
      manufacturer: 'KSB',
      model: 'Etanorm 125-100-250',
      serialNumber: 'KSB123456',
      installationYear: 2020,
      lastInspection: '2024-06-25',
      nextInspection: '2024-06-28'
    },
    {
      id: 2,
      code: 'EQ-002',
      name: 'Motor Elétrico B2',
      description: 'Motor elétrico trifásico 50HP',
      location: 'Área 2 - Setor B',
      area: 'Área 2',
      sector: 'Setor B',
      type: 'MOTOR',
      status: 'ACTIVE',
      manufacturer: 'WEG',
      model: 'W22 Premium',
      serialNumber: 'WEG789012',
      installationYear: 2019,
      lastInspection: '2024-06-26',
      nextInspection: '2024-06-29'
    },
    {
      id: 3,
      code: 'EQ-003',
      name: 'Compressor C1',
      description: 'Compressor de ar rotativo',
      location: 'Área 1 - Setor C',
      area: 'Área 1',
      sector: 'Setor C',
      type: 'COMPRESSOR',
      status: 'MAINTENANCE',
      manufacturer: 'Atlas Copco',
      model: 'GA 37',
      serialNumber: 'AC345678',
      installationYear: 2018,
      lastInspection: '2024-06-20',
      nextInspection: '2024-07-01'
    },
    {
      id: 4,
      code: 'EQ-004',
      name: 'Válvula de Controle D1',
      description: 'Válvula de controle pneumática',
      location: 'Área 3 - Setor D',
      area: 'Área 3',
      sector: 'Setor D',
      type: 'VALVE',
      status: 'ACTIVE',
      manufacturer: 'Emerson',
      model: 'Fisher EZ',
      serialNumber: 'EM901234',
      installationYear: 2021,
      lastInspection: '2024-06-24',
      nextInspection: '2024-06-27'
    }
  ])

  const [filteredEquipments, setFilteredEquipments] = useState(equipments)
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState('ALL')
  const [areaFilter, setAreaFilter] = useState('ALL')

  useEffect(() => {
    let filtered = equipments

    // Filter by search term
    if (searchTerm) {
      filtered = filtered.filter(equipment =>
        equipment.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
        equipment.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        equipment.location.toLowerCase().includes(searchTerm.toLowerCase())
      )
    }

    // Filter by status
    if (statusFilter !== 'ALL') {
      filtered = filtered.filter(equipment => equipment.status === statusFilter)
    }

    // Filter by area
    if (areaFilter !== 'ALL') {
      filtered = filtered.filter(equipment => equipment.area === areaFilter)
    }

    setFilteredEquipments(filtered)
  }, [searchTerm, statusFilter, areaFilter, equipments])

  const getStatusBadge = (status) => {
    switch (status) {
      case 'ACTIVE':
        return <Badge className="bg-green-100 text-green-800">Ativo</Badge>
      case 'INACTIVE':
        return <Badge className="bg-gray-100 text-gray-800">Inativo</Badge>
      case 'MAINTENANCE':
        return <Badge className="bg-yellow-100 text-yellow-800">Manutenção</Badge>
      case 'DECOMMISSIONED':
        return <Badge className="bg-red-100 text-red-800">Descomissionado</Badge>
      default:
        return <Badge variant="secondary">{status}</Badge>
    }
  }

  const getTypeLabel = (type) => {
    const types = {
      MOTOR: 'Motor',
      PUMP: 'Bomba',
      COMPRESSOR: 'Compressor',
      VALVE: 'Válvula',
      TANK: 'Tanque',
      CONVEYOR: 'Esteira',
      BOILER: 'Caldeira',
      TURBINE: 'Turbina',
      GENERATOR: 'Gerador',
      OTHER: 'Outro'
    }
    return types[type] || type
  }

  const uniqueAreas = [...new Set(equipments.map(eq => eq.area))]

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Equipamentos</h1>
          <p className="text-gray-600">Gerencie os equipamentos da fábrica</p>
        </div>
        <Button className="bg-green-600 hover:bg-green-700">
          <Plus className="h-4 w-4 mr-2" />
          Novo Equipamento
        </Button>
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
                  placeholder="Código, nome ou localização..."
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
                <option value="ACTIVE">Ativo</option>
                <option value="INACTIVE">Inativo</option>
                <option value="MAINTENANCE">Manutenção</option>
                <option value="DECOMMISSIONED">Descomissionado</option>
              </select>
            </div>
            <div>
              <label className="text-sm font-medium mb-2 block">Área</label>
              <select
                value={areaFilter}
                onChange={(e) => setAreaFilter(e.target.value)}
                className="w-full p-2 border border-gray-300 rounded-md"
              >
                <option value="ALL">Todas</option>
                {uniqueAreas.map(area => (
                  <option key={area} value={area}>{area}</option>
                ))}
              </select>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Equipment Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredEquipments.map((equipment) => (
          <Card key={equipment.id} className="hover:shadow-lg transition-shadow">
            <CardHeader>
              <div className="flex justify-between items-start">
                <div>
                  <CardTitle className="text-lg">{equipment.code}</CardTitle>
                  <CardDescription>{equipment.name}</CardDescription>
                </div>
                {getStatusBadge(equipment.status)}
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                <div className="flex items-center text-sm text-gray-600">
                  <MapPin className="h-4 w-4 mr-2" />
                  {equipment.location}
                </div>
                
                <div className="flex items-center text-sm text-gray-600">
                  <Settings className="h-4 w-4 mr-2" />
                  {getTypeLabel(equipment.type)}
                </div>

                <div className="text-sm">
                  <div className="text-gray-600">Fabricante: {equipment.manufacturer}</div>
                  <div className="text-gray-600">Modelo: {equipment.model}</div>
                </div>

                <div className="text-sm">
                  <div className="text-gray-600">Última inspeção: {equipment.lastInspection}</div>
                  <div className="text-gray-600">Próxima inspeção: {equipment.nextInspection}</div>
                </div>

                <div className="flex space-x-2 pt-2">
                  <Button variant="outline" size="sm" className="flex-1">
                    <QrCode className="h-4 w-4 mr-2" />
                    QR Code
                  </Button>
                  <Link to={`/inspection/new/${equipment.id}`} className="flex-1">
                    <Button size="sm" className="w-full bg-green-600 hover:bg-green-700">
                      <Eye className="h-4 w-4 mr-2" />
                      Inspecionar
                    </Button>
                  </Link>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredEquipments.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <Settings className="h-12 w-12 text-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Nenhum equipamento encontrado</h3>
            <p className="text-gray-600">Tente ajustar os filtros ou adicionar novos equipamentos.</p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}

export default EquipmentList

