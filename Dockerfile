FROM openjdk:8

COPY target/latest/ /TAI/

CMD /TAI/bin/tai -Dhttps.port=9443 -Dplay.http.secret.key='QCY?tAnfk?aZ?iwrNwnxIlR6CTf:G3gf:90Latabg@5241AB`R5W:1uDFN];Ik@n'

EXPOSE 9000 9443
