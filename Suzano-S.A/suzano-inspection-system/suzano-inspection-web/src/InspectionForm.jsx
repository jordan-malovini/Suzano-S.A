import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { Badge } from '@/components/ui/badge'
import { Alert, AlertDescription } from '@/components/ui/alert'
import { 
  Camera, 
  MapPin, 
  QrCode, 
  CheckCircle, 
  AlertTriangle, 
  X,
  Upload,
  Play,
  Square,
  Save,
  Send
} from 'lucide-react'

const InspectionForm = () => {
  const { id, equipmentId } = useParams()
  const navigate = useNavigate()
  const [isNewInspection, setIsNewInspection] = useState(!id)
  
  const [equipment, setEquipment] = useState({
    id: 1,
    code: 'EQ-001',
    name: 'Bomba Centrífuga A1',
    description: 'Bomba centrífuga para transferência de líquidos',
    location: 'Área 1 - Setor A',
    manufacturer: 'KSB',
    model: 'Etanorm 125-100-250'
  })

  const [inspection, setInspection] = useState({
    status: 'PENDING',
    startedAt: null,
    observations: '',
    gpsLocation: null,
    mediaFiles: [],
    checklistItems: [
      { id: 1, name: 'Verificar vazamentos', status: 'NOT_CHECKED', observations: '', isCompliant: null },
      { id: 2, name: 'Verificar ruídos anormais', status: 'NOT_CHECKED', observations: '', isCompliant: null },
      { id: 3, name: 'Verificar vibração', status: 'NOT_CHECKED', observations: '', isCompliant: null },
      { id: 4, name: 'Verificar temperatura', status: 'NOT_CHECKED', observations: '', isCompliant: null },
      { id: 5, name: 'Verificar pressão', status: 'NOT_CHECKED', observations: '', isCompliant: null },
      { id: 6, name: 'Verificar lubrificação', status: 'NOT_CHECKED', observations: '', isCompliant: null }
    ]
  })

  const [currentLocation, setCurrentLocation] = useState(null)
  const [isRecording, setIsRecording] = useState(false)

  useEffect(() => {
    // Get current GPS location
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setCurrentLocation({
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
          })
        },
        (error) => {
          console.error('Error getting location:', error)
        }
      )
    }
  }, [])

  const startInspection = () => {
    setInspection(prev => ({
      ...prev,
      status: 'IN_PROGRESS',
      startedAt: new Date().toISOString(),
      gpsLocation: currentLocation
    }))
  }

  const handleChecklistItemChange = (itemId, field, value) => {
    setInspection(prev => ({
      ...prev,
      checklistItems: prev.checklistItems.map(item =>
        item.id === itemId ? { ...item, [field]: value } : item
      )
    }))
  }

  const handleFileUpload = (event) => {
    const files = Array.from(event.target.files)
    const newMediaFiles = files.map(file => ({
      id: Date.now() + Math.random(),
      name: file.name,
      type: file.type.startsWith('image/') ? 'PHOTO' : 'VIDEO',
      url: URL.createObjectURL(file),
      capturedAt: new Date().toISOString(),
      gpsLocation: currentLocation
    }))

    setInspection(prev => ({
      ...prev,
      mediaFiles: [...prev.mediaFiles, ...newMediaFiles]
    }))
  }

  const removeMediaFile = (fileId) => {
    setInspection(prev => ({
      ...prev,
      mediaFiles: prev.mediaFiles.filter(file => file.id !== fileId)
    }))
  }

  const completeInspection = () => {
    const completedItems = inspection.checklistItems.filter(item => item.status !== 'NOT_CHECKED').length
    const totalItems = inspection.checklistItems.length
    const criticalItems = inspection.checklistItems.filter(item => item.status === 'CRITICAL').length
    const nonCompliantItems = inspection.checklistItems.filter(item => item.isCompliant === false).length

    let result = 'APPROVED'
    if (criticalItems > 0) {
      result = 'CRITICAL'
    } else if (nonCompliantItems > 0) {
      result = 'REQUIRES_MAINTENANCE'
    } else if (inspection.observations.trim()) {
      result = 'APPROVED_WITH_OBSERVATIONS'
    }

    setInspection(prev => ({
      ...prev,
      status: 'COMPLETED',
      result: result,
      completedAt: new Date().toISOString()
    }))

    // Simulate API call to save inspection
    setTimeout(() => {
      alert('Inspeção concluída com sucesso!')
      navigate('/inspections')
    }, 1000)
  }

  const getItemStatusBadge = (status) => {
    switch (status) {
      case 'APPROVED':
        return <Badge className="bg-green-100 text-green-800">Aprovado</Badge>
      case 'REQUIRES_ATTENTION':
        return <Badge className="bg-yellow-100 text-yellow-800">Atenção</Badge>
      case 'CRITICAL':
        return <Badge className="bg-red-100 text-red-800">Crítico</Badge>
      case 'NOT_APPLICABLE':
        return <Badge className="bg-gray-100 text-gray-800">N/A</Badge>
      default:
        return <Badge variant="secondary">Não Verificado</Badge>
    }
  }

  const getComplianceBadge = (isCompliant) => {
    if (isCompliant === true) {
      return <Badge className="bg-green-100 text-green-800">Conforme</Badge>
    } else if (isCompliant === false) {
      return <Badge className="bg-red-100 text-red-800">Não Conforme</Badge>
    }
    return null
  }

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* Header */}
      <div className="flex justify-between items-start">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">
            {isNewInspection ? 'Nova Inspeção' : 'Inspeção'}
          </h1>
          <p className="text-gray-600">Equipamento: {equipment.code} - {equipment.name}</p>
        </div>
        <div className="flex items-center space-x-2">
          {inspection.status === 'PENDING' && (
            <Button onClick={startInspection} className="bg-green-600 hover:bg-green-700">
              <Play className="h-4 w-4 mr-2" />
              Iniciar Inspeção
            </Button>
          )}
          {inspection.status === 'IN_PROGRESS' && (
            <Badge className="bg-blue-100 text-blue-800">Em Andamento</Badge>
          )}
          {inspection.status === 'COMPLETED' && (
            <Badge className="bg-green-100 text-green-800">Concluída</Badge>
          )}
        </div>
      </div>

      {/* Equipment Info */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center space-x-2">
            <QrCode className="h-5 w-5" />
            <span>Informações do Equipamento</span>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="text-sm font-medium text-gray-600">Código</label>
              <p className="text-lg font-semibold">{equipment.code}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-gray-600">Nome</label>
              <p className="text-lg">{equipment.name}</p>
            </div>
            <div>
              <label className="text-sm font-medium text-gray-600">Localização</label>
              <p className="flex items-center">
                <MapPin className="h-4 w-4 mr-1" />
                {equipment.location}
              </p>
            </div>
            <div>
              <label className="text-sm font-medium text-gray-600">Fabricante/Modelo</label>
              <p>{equipment.manufacturer} - {equipment.model}</p>
            </div>
          </div>
          
          {currentLocation && (
            <div className="mt-4 p-3 bg-green-50 rounded-md">
              <p className="text-sm text-green-800">
                <MapPin className="h-4 w-4 inline mr-1" />
                GPS: {currentLocation.latitude.toFixed(6)}, {currentLocation.longitude.toFixed(6)}
              </p>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Checklist */}
      {inspection.status !== 'PENDING' && (
        <Card>
          <CardHeader>
            <CardTitle>Lista de Verificação</CardTitle>
            <CardDescription>Marque cada item conforme realiza a verificação</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {inspection.checklistItems.map((item) => (
                <div key={item.id} className="border rounded-lg p-4">
                  <div className="flex items-center justify-between mb-3">
                    <h4 className="font-medium">{item.name}</h4>
                    <div className="flex items-center space-x-2">
                      {getItemStatusBadge(item.status)}
                      {getComplianceBadge(item.isCompliant)}
                    </div>
                  </div>
                  
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <label className="text-sm font-medium mb-2 block">Status</label>
                      <select
                        value={item.status}
                        onChange={(e) => handleChecklistItemChange(item.id, 'status', e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded-md"
                        disabled={inspection.status === 'COMPLETED'}
                      >
                        <option value="NOT_CHECKED">Não Verificado</option>
                        <option value="APPROVED">Aprovado</option>
                        <option value="REQUIRES_ATTENTION">Requer Atenção</option>
                        <option value="CRITICAL">Crítico</option>
                        <option value="NOT_APPLICABLE">Não Aplicável</option>
                      </select>
                    </div>
                    
                    <div>
                      <label className="text-sm font-medium mb-2 block">Conformidade</label>
                      <select
                        value={item.isCompliant === null ? '' : item.isCompliant.toString()}
                        onChange={(e) => handleChecklistItemChange(item.id, 'isCompliant', e.target.value === '' ? null : e.target.value === 'true')}
                        className="w-full p-2 border border-gray-300 rounded-md"
                        disabled={inspection.status === 'COMPLETED'}
                      >
                        <option value="">Selecione</option>
                        <option value="true">Conforme</option>
                        <option value="false">Não Conforme</option>
                      </select>
                    </div>
                    
                    <div>
                      <label className="text-sm font-medium mb-2 block">Observações</label>
                      <Input
                        value={item.observations}
                        onChange={(e) => handleChecklistItemChange(item.id, 'observations', e.target.value)}
                        placeholder="Observações específicas..."
                        disabled={inspection.status === 'COMPLETED'}
                      />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Media Capture */}
      {inspection.status !== 'PENDING' && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center space-x-2">
              <Camera className="h-5 w-5" />
              <span>Captura de Mídia</span>
            </CardTitle>
            <CardDescription>Tire fotos e grave vídeos do equipamento</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex space-x-2">
                <label className="flex-1">
                  <input
                    type="file"
                    multiple
                    accept="image/*,video/*"
                    onChange={handleFileUpload}
                    className="hidden"
                    disabled={inspection.status === 'COMPLETED'}
                  />
                  <Button variant="outline" className="w-full" disabled={inspection.status === 'COMPLETED'}>
                    <Camera className="h-4 w-4 mr-2" />
                    Capturar Foto/Vídeo
                  </Button>
                </label>
              </div>

              {inspection.mediaFiles.length > 0 && (
                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                  {inspection.mediaFiles.map((file) => (
                    <div key={file.id} className="relative border rounded-lg overflow-hidden">
                      {file.type === 'PHOTO' ? (
                        <img
                          src={file.url}
                          alt={file.name}
                          className="w-full h-32 object-cover"
                        />
                      ) : (
                        <video
                          src={file.url}
                          className="w-full h-32 object-cover"
                          controls
                        />
                      )}
                      {inspection.status !== 'COMPLETED' && (
                        <button
                          onClick={() => removeMediaFile(file.id)}
                          className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1 hover:bg-red-600"
                        >
                          <X className="h-3 w-3" />
                        </button>
                      )}
                      <div className="p-2">
                        <p className="text-xs text-gray-600 truncate">{file.name}</p>
                        <p className="text-xs text-gray-500">{new Date(file.capturedAt).toLocaleString('pt-BR')}</p>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      )}

      {/* General Observations */}
      {inspection.status !== 'PENDING' && (
        <Card>
          <CardHeader>
            <CardTitle>Observações Gerais</CardTitle>
            <CardDescription>Adicione observações gerais sobre a inspeção</CardDescription>
          </CardHeader>
          <CardContent>
            <Textarea
              value={inspection.observations}
              onChange={(e) => setInspection(prev => ({ ...prev, observations: e.target.value }))}
              placeholder="Digite suas observações sobre o estado geral do equipamento..."
              rows={4}
              disabled={inspection.status === 'COMPLETED'}
            />
          </CardContent>
        </Card>
      )}

      {/* Action Buttons */}
      {inspection.status === 'IN_PROGRESS' && (
        <div className="flex space-x-4">
          <Button variant="outline" className="flex-1">
            <Save className="h-4 w-4 mr-2" />
            Salvar Rascunho
          </Button>
          <Button onClick={completeInspection} className="flex-1 bg-green-600 hover:bg-green-700">
            <Send className="h-4 w-4 mr-2" />
            Concluir Inspeção
          </Button>
        </div>
      )}

      {inspection.status === 'COMPLETED' && (
        <Alert>
          <CheckCircle className="h-4 w-4" />
          <AlertDescription>
            Inspeção concluída com sucesso! Resultado: {inspection.result}
          </AlertDescription>
        </Alert>
      )}
    </div>
  )
}

export default InspectionForm

