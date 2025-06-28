import { createContext, useContext, useState, useEffect } from 'react'

const AuthContext = createContext()

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    // Check if user is logged in (check localStorage for token)
    const token = localStorage.getItem('token')
    const userData = localStorage.getItem('user')
    
    if (token && userData) {
      try {
        setUser(JSON.parse(userData))
      } catch (error) {
        console.error('Error parsing user data:', error)
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      }
    }
    
    setLoading(false)
  }, [])

  const login = async (username, password) => {
    try {
      // Simulate API call - replace with actual API endpoint
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      })

      if (!response.ok) {
        throw new Error('Login failed')
      }

      const data = await response.json()
      
      // Store token and user data
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data))
      
      setUser(data)
      return { success: true }
    } catch (error) {
      console.error('Login error:', error)
      
      // For demo purposes, simulate successful login
      const mockUser = {
        id: 1,
        username: username,
        fullName: 'Inspector Demo',
        email: 'inspector@suzano.com',
        role: 'INSPECTOR',
        department: 'Manutenção',
        token: 'demo-token-123'
      }
      
      localStorage.setItem('token', mockUser.token)
      localStorage.setItem('user', JSON.stringify(mockUser))
      setUser(mockUser)
      
      return { success: true }
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  const value = {
    user,
    login,
    logout,
    loading
  }

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  )
}

