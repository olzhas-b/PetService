FROM golang:1.16-alpine

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY . ./

RUN go build -o /backEnd

EXPOSE 8080

ENV HTTP_PORT=8080

ENTRYPOINT ["/backEnd", "start"]