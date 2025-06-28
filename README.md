# Sistema de Inspeção de Equipamentos Suzano

## 🏭 Visão Geral

O Sistema de Inspeção de Equipamentos da Suzano é uma solução tecnológica moderna desenvolvida para digitalizar e otimizar os processos de inspeção de equipamentos industriais. O sistema elimina erros de identificação, garante precisão na coleta de dados e permite armazenamento seguro de informações multimídia.

## 🚀 Funcionalidades Principais

- **Identificação Precisa**: QR Codes únicos para cada equipamento
- **Captura de Localização**: GPS automático para validação de localização
- **Formulários Digitais**: Checklists customizáveis e obrigatórios
- **Mídia Integrada**: Captura e armazenamento de fotos e vídeos
- **Relatórios Automáticos**: Geração automática de relatórios de inspeção
- **Alertas em Tempo Real**: Notificações para situações críticas
- **Interface Responsiva**: Funciona em dispositivos móveis e desktop

## 🏗️ Arquitetura

### Tecnologias Utilizadas

- **Backend**: Java 17 + Spring Boot 3.x
- **Frontend**: React 18 + Vite
- **Banco de Dados**: PostgreSQL 15
- **Cloud**: Amazon Web Services (AWS)
- **Containerização**: Docker
- **Infraestrutura**: Terraform (Infrastructure as Code)

### Componentes AWS

- **ECS Fargate**: Execução de containers
- **RDS PostgreSQL**: Banco de dados gerenciado
- **S3**: Armazenamento de arquivos multimídia
- **CloudFront**: CDN para distribuição de conteúdo
- **Application Load Balancer**: Balanceamento de carga
- **CloudWatch**: Monitoramento e logs
- **WAF**: Proteção contra ataques web

## 📁 Estrutura do Projeto

```
suzano-inspection-system/
├── inspection-backend/          # Backend Java Spring Boot
│   ├── src/main/java/          # Código fonte Java
│   ├── src/main/resources/     # Configurações e recursos
│   ├── Dockerfile              # Container Docker
│   └── pom.xml                 # Dependências Maven
├── suzano-inspection-web/       # Frontend React
│   ├── src/                    # Código fonte React
│   ├── public/                 # Arquivos públicos
│   └── package.json            # Dependências NPM
├── aws-infrastructure/          # Infraestrutura AWS
│   ├── terraform/              # Configurações Terraform
│   ├── scripts/                # Scripts de deployment
│   └── outputs.json            # Outputs do Terraform
├── DEPLOYMENT_GUIDE.md         # Guia completo de implantação
└── README.md                   # Este arquivo
```

## 🛠️ Pré-requisitos

### Ferramentas Necessárias

- **Java 17+** (OpenJDK ou Oracle JDK)
- **Maven 3.6+** (Gerenciamento de dependências)
- **Node.js 18+** (Runtime JavaScript)
- **Docker 20.0+** (Containerização)
- **Terraform 1.0+** (Infrastructure as Code)
- **AWS CLI 2.0+** (Interface de linha de comando AWS)

### Conta AWS

- Conta AWS ativa com permissões administrativas
- AWS CLI configurado com credenciais válidas
- Limites de serviço adequados para a infraestrutura

## 🚀 Início Rápido

### 1. Clonar o Repositório

```bash
git clone https://github.com/suzano/inspection-system.git
cd suzano-inspection-system
```

### 2. Configurar Variáveis de Ambiente

```bash
cp aws-infrastructure/terraform/terraform.tfvars.example aws-infrastructure/terraform/terraform.tfvars
# Editar terraform.tfvars com suas configurações
```

### 3. Executar Deployment Automatizado

```bash
cd aws-infrastructure/scripts
chmod +x deploy.sh
./deploy.sh --region us-east-1 --environment prod --email seu-email@suzano.com.br
```

### 4. Acessar a Aplicação

Após o deployment, a URL da aplicação será exibida no terminal. Acesse usando as credenciais padrão:
- **Usuário**: `inspector`
- **Senha**: `123456`

## 📖 Documentação Detalhada

Para informações completas sobre implantação, operação e manutenção, consulte o [Guia de Implantação](DEPLOYMENT_GUIDE.md).

## 🔧 Desenvolvimento Local

### Backend (Spring Boot)

```bash
cd inspection-backend
mvn spring-boot:run -Dspring.profiles.active=dev
```

A aplicação estará disponível em `http://localhost:8080`

### Frontend (React)

```bash
cd suzano-inspection-web
npm install
npm run dev
```

A interface estará disponível em `http://localhost:5173`

## 🧪 Testes

### Testes Unitários

```bash
# Backend
cd inspection-backend
mvn test

# Frontend
cd suzano-inspection-web
npm test
```

### Testes de Integração

```bash
cd inspection-backend
mvn test -Dspring.profiles.active=integration
```

## 📊 Monitoramento

O sistema inclui monitoramento abrangente através do AWS CloudWatch:

- **Métricas de Performance**: CPU, memória, latência
- **Logs Centralizados**: Aplicação, infraestrutura e segurança
- **Alertas Automáticos**: Notificações para situações críticas
- **Dashboard Customizado**: Visão consolidada do sistema

## 🔒 Segurança

- **Criptografia**: Dados criptografados em trânsito e em repouso
- **Autenticação**: JWT tokens para autenticação de usuários
- **Autorização**: Controle de acesso baseado em roles
- **WAF**: Proteção contra ataques web comuns
- **Auditoria**: Logs completos de todas as ações

## 💰 Custos Estimados

| Ambiente | Custo Mensal (USD) |
|----------|-------------------|
| Desenvolvimento | $150 |
| Produção | $370 |

*Custos baseados na região us-east-1 com uso moderado*

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um Pull Request

## 📝 Licença

Este projeto é propriedade da Suzano S.A. e está licenciado sob os termos internos da empresa.

## 📞 Suporte

Para suporte técnico ou dúvidas sobre o sistema:

- **Email**: devops@suzano.com.br
- **Slack**: #suzano-inspection-system
- **Documentação**: [Confluence - Sistema de Inspeção](https://suzano.atlassian.net/wiki/spaces/INSP)

## 🔄 Versionamento

Este projeto segue o [Semantic Versioning](https://semver.org/):

- **MAJOR**: Mudanças incompatíveis na API
- **MINOR**: Novas funcionalidades compatíveis
- **PATCH**: Correções de bugs compatíveis

Versão atual: **1.0.0**

## 📈 Roadmap

### Versão 1.1 (Q3 2025)
- [ ] Integração com SAP
- [ ] Relatórios avançados com BI
- [ ] App móvel nativo (iOS/Android)

### Versão 1.2 (Q4 2025)
- [ ] Análise preditiva com ML
- [ ] Integração com IoT sensors
- [ ] Dashboard executivo

### Versão 2.0 (Q1 2026)
- [ ] Arquitetura de microserviços
- [ ] Multi-tenancy
- [ ] API pública para integrações

---

**Desenvolvido com ❤️ para a Suzano S.A.**
