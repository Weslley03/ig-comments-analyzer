# ig comments analyzer

> projeto de estudos em evolução. o objetivo principal é explorar na prática o uso de web scraping com automação de browser e integração com llms via api.

---

## sobre o projeto

a ideia central é simples: dado o link de uma publicação **pública** do instagram, a aplicação coleta os comentários da postagem usando automação de browser e os envia para análise de sentimento via llm (através da api do openrouter). o resultado é retornado em formato estruturado, descrevendo percepções do público, críticas, elogios e um nível geral de engajamento.

---

## tecnologias utilizadas

- **java 17**
- **spring boot 4.x** — estrutura da api rest
- **playwright (java)** — automação de browser para coleta dos comentários
- **openrouter api** — acesso a modelos de linguagem (llm) via http
- **maven** — gerenciamento de dependências e build

---

## funcionalidades

- recebe a url de uma publicação pública do instagram via parâmetro de query
- abre o post com playwright (browser headless) e extrai os comentários
- envia os comentários coletados para análise de sentimento usando um llm configurável
- retorna um relatório estruturado com:
  - sentimento predominante do público
  - principais percepções recorrentes
  - críticas e elogios relevantes
  - nível de engajamento percebido
  - conclusão estratégica

---

## fluxo de uso

```
GET /instagram/comments?url=https://www.instagram.com/p/XXXXXXXX
```

1. a api valida a url recebida
2. o playwright abre a publicação no browser e coleta os comentários visíveis
3. os comentários são enviados ao llm via openrouter
4. a resposta da análise é retornada no body em formato json

**exemplo de resposta:**

```json
{
  "status": 200,
  "message": "análise concluída com sucesso.",
  "data": "1. sentimento predominante: negativo.\n2. principais percepções..."
}
```

---

## configuração

### pré-requisitos

- java 17+
- maven
- uma **chave de api própria do openrouter** (necessária para autenticar as requisições ao llm)

### como configurar

1. copie o arquivo de exemplo:
   ```bash
   cp src/main/resources/application-example.properties src/main/resources/application.properties
   ```

2. preencha com sua chave:
   ```properties
   openrouter.api.key=sua_chave_aqui
   openrouter.model=openai/gpt-4o-mini
   ```

> **importante:** a chave do openrouter é pessoal e não está inclusa no projeto. você precisa criar uma conta em [openrouter.ai](https://openrouter.ai) e gerar sua própria key.

### executando

```bash
./mvnw spring-boot:run
```

---

## exemplo visual

imagem de capa de um post utilizado como entrada:

![exemplo de post](capa-post-example.png)

resultado da request:
1. **Sentimento predominante do público:** Negativo.
2. **Principais percepções recorrentes:** O público expressa insatisfação com a decisão do Tribunal de Justiça que suspendeu a alteração da data do feriado de aniversário de Maringá, defendendo que o feriado deveria permanecer no dia 10, que cai em um domingo. Há um sentimento geral de frustração em relação ao desinteresse das autoridades em considerar as necessidades dos trabalhadores, e muitos comentários sugerem uma crítica contundente à interferência do empresariado nas decisões que afetam o trabalhador.
3. **Críticas relevantes:** As críticas se concentram na percepção de que os interesses dos trabalhadores estão sendo desconsiderados em prol dos comerciantes, além de questionamentos sobre a legitimidade da mudança da data do feriado. Há uma insatisfação com a forma como a situação foi tratada pelo legislativo e judiciário, sugerindo que o sistema favorece uma classe em detrimento da classe trabalhadora.
4. **Elogios relevantes:** Elogios são raros, mas alguns comentários reconhecem a decisão do Judiciário como sensata e justificada, ainda que a maioria do público perceba essa decisão sob uma luz negativa.
5. **Nível de engajamento percebido:** Alto. Muitos usuários expressam opiniões fortes e participam ativamente da discussão, compartilhando suas frustrações e propostas de boicote.
6. **Conclusão estratégica:** Dada a forte insatisfação e o engajamento do público, é crucial que as autoridades locais considerem uma comunicação mais clara e uma abordagem proativa em relação às preocupações da comunidade, especialmente dos trabalhadores. Estar atento às demandas populares pode ajudar a restaurar a confiança nas instituições e a promover uma maior harmonia entre os interesses empresariais e os direitos dos trabalhadores. A falta de reconhecimento das necessidades da classe trabalhadora pode alimentar ainda mais descontentamento e desconfiança nas decisões futuras.

---

## status do projeto

projeto em evolução ativo.

---

## aviso

este projeto é exclusivamente para fins educacionais. o uso de web scraping deve respeitar os termos de serviço das plataformas. não utilize para fins comerciais ou em contas privadas sem autorização.
