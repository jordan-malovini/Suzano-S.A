import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'
import { 
  ClipboardList, 
  Settings, 
  CheckCircle, 
  AlertTriangle, 
  Clock,
  TrendingUp,
  Calendar,
  MapPin
} from 'lucide-react'
import { useAuth } from '../contexts/AuthContext'

const Dashboard = () => {
  const { user } = useAuth()
  const [stats, setStats] = useState({
    pendingInspections: 12,
    completedToday: 8,
    totalEquipments: 156,
    criticalIssues: 3
  })

  const [recentInspections, setRecentInspections] = useState([
    {
      id: 1,
      equipmentCode: 'EQ-001',
      equipmentName: 'Bomba Centrífuga A1',
      location: 'Área 1 - Setor A',
      status: 'completed',
      result: 'approved',
      completedAt: '2024-06-27 14:30'
    },
    {
      id: 2,
      equipmentCode: 'EQ-002',
      equipmentName: 'Motor Elétrico B2',
      location: 'Área 2 - Setor B',
      status: 'in_progress',
      startedAt: '2024-06-27 15:00'
    },
    {
      id: 3,
      equipmentCode: 'EQ-003',
      equipmentName: 'Compressor C1',
      location: 'Área 1 - Setor C',
      status: 'pending',
      scheduledDate: '2024-06-27 16:00'
    }
  ])

  const [pendingInspections, setPendingInspections] = useState([
    {
      id: 4,
      equipmentCode: 'EQ-004',
      equipmentName: 'Válvula de Controle D1',
      location: 'Área 3 - Setor D',
      scheduledDate: '2024-06-27 16:30',
      priority: 'high'
    },
    {
      id: 5,
      equipmentCode: 'EQ-005',
      equipmentName: 'Tanque de Armazenamento E1',
      location: 'Área 2 - Setor E',
      scheduledDate: '2024-06-27 17:00',
      priority: 'medium'
    }
  ])

  const getStatusBadge = (status, result) => {
    if (status === 'completed') {
      if (result === 'approved') {
        return <Badge className="bg-green-100 text-green-800">Aprovado</Badge>
      } else if (result === 'requires_maintenance') {
        return <Badge className="bg-yellow-100 text-yellow-800">Requer Manutenção</Badge>
      } else if (result === 'critical') {
        return <Badge className="bg-red-100 text-red-800">Crítico</Badge>
      }
    } else if (status === 'in_progress') {
      return <Badge className="bg-blue-100 text-blue-800">Em Andamento</Badge>
    } else if (status === 'pending') {
      return <Badge className="bg-gray-100 text-gray-800">Pendente</Badge>
    }
    return null
  }

  const getPriorityBadge = (priority) => {
    if (priority === 'high') {
      return <Badge variant="destructive">Alta</Badge>
    } else if (priority === 'medium') {
      return <Badge className="bg-yellow-100 text-yellow-800">Média</Badge>
    } else {
      return <Badge variant="secondary">Baixa</Badge>
    }
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-600">Bem-vindo, {user?.fullName}</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Inspeções Pendentes</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-orange-600">{stats.pendingInspections}</div>
            <p className="text-xs text-muted-foreground">Para hoje</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Concluídas Hoje</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">{stats.completedToday}</div>
            <p className="text-xs text-muted-foreground">+2 desde ontem</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Equipamentos</CardTitle>
            <Settings className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalEquipments}</div>
            <p className="text-xs text-muted-foreground">Ativos no sistema</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Questões Críticas</CardTitle>
            <AlertTriangle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">{stats.criticalIssues}</div>
            <p className="text-xs text-muted-foreground">Requer atenção imediata</p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent Inspections */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <ClipboardList className="h-5 w-5" />
              <span>Inspeções Recentes</span>
            </CardTitle>
            <CardDescription>Últimas inspeções realizadas</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {recentInspections.map((inspection) => (
                <div key={inspection.id} className="flex items-center justify-between p-3 border rounded-lg">
                  <div className="flex-1">
                    <div className="font-medium">{inspection.equipmentCode}</div>
                    <div className="text-sm text-gray-600">{inspection.equipmentName}</div>
                    <div className="flex items-center text-xs text-gray-500 mt-1">
                      <MapPin className="h-3 w-3 mr-1" />
                      {inspection.location}
                    </div>
                  </div>
                  <div className="text-right">
                    {getStatusBadge(inspection.status, inspection.result)}
                    <div className="text-xs text-gray-500 mt-1">
                      {inspection.completedAt || inspection.startedAt || inspection.scheduledDate}
                    </div>
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-4">
              <Link to="/inspections">
                <Button variant="outline" className="w-full">
                  Ver Todas as Inspeções
                </Button>
              </Link>
            </div>
          </CardContent>
        </Card>

        {/* Pending Inspections */}
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Calendar className="h-5 w-5" />
              <span>Próximas Inspeções</span>
            </CardTitle>
            <CardDescription>Inspeções agendadas para hoje</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {pendingInspections.map((inspection) => (
                <div key={inspection.id} className="flex items-center justify-between p-3 border rounded-lg">
                  <div className="flex-1">
                    <div className="font-medium">{inspection.equipmentCode}</div>
                    <div className="text-sm text-gray-600">{inspection.equipmentName}</div>
                    <div className="flex items-center text-xs text-gray-500 mt-1">
                      <MapPin className="h-3 w-3 mr-1" />
                      {inspection.location}
                    </div>
                  </div>
                  <div className="text-right">
                    {getPriorityBadge(inspection.priority)}
                    <div className="text-xs text-gray-500 mt-1">
                      {inspection.scheduledDate}
                    </div>
                    <Link to={`/inspection/new/${inspection.id}`}>
                      <Button size="sm" className="mt-2 bg-green-600 hover:bg-green-700">
                        Iniciar
                      </Button>
                    </Link>
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-4">
              <Link to="/inspections?status=pending">
                <Button variant="outline" className="w-full">
                  Ver Todas Pendentes
                </Button>
              </Link>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

export default Dashboard

