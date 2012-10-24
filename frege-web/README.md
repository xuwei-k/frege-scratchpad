# A Servlet-Based Web Application with the Frege Programming Language

*30 September, 2012*

Since [Frege](http://code.google.com/p/frege/) runs atop the JVM, I thought it would be interesting to build a little Frege-based Web application, hook it into the Java Servlet API, and get it running under Jetty and Tomcat.

To start, we need some simple data types to represent HTTP requests and responses.

```haskell
data Request = Request { method :: String, uri :: String, headers :: [Header], parameters :: [Parameter], body :: [Byte] }
data Response = Response { status :: Int, headers :: [Header], parameters :: [Parameter], body :: [Byte] }
data Header = Header { name :: String, value :: String }
data Parameter = Parameter { name :: String, value :: String }
```

This requires a new `Byte` data type.  While we're at it, let's add a way to convert (via casting) `char`s to `byte`s.

```haskell
data Byte = native byte
pure native byte "(byte)" :: Char -> Byte
```

Next, some syntactic sweetness to create `Response` instances:

```haskell
response :: Int -> [Header] -> [Parameter] -> [Byte] -> Response
response status headers parameters body = Response { status = status, headers = headers, parameters = parameters, body = body }
```

Now the fun part - defining the HTTP endpoints, and binding them to specific behavior.

```haskell
service :: Request -> Response
service (Request "GET" "/" _ _ _) = response 200 [] [] $ map byte $ unpacked "<a href=\"hello\">hello</a>"
service (Request "GET" "/hello" _ _ _) = response 200 [] [] $ map byte $ unpacked "Hello, world!"
service _ = response 404 [] [] $ map byte $ unpacked "404'd!"
```

For HTTP GET requests to */*, this service responds with an HTML link to */hello*.  For HTTP GET requests to */hello*, it responds with the string *Hello, world!*.  For any other request, it responds with a *404* error.

With a bit of trial-and-error, it is possible to bind this Frege code into the Java Servlet API.

```java
public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {

  TRequest request = TRequest.mk(Box.mk("GET"), Box.mk(uriOf(req)), DList.mk(), DList.mk(), Box.mk(TList.DList.mk()));
  TResponse response = (TResponse) FregeWeb.service(request);

  TList responseBodyL = (TList) TResponse.body(response);
  write(responseBodyL, res.getOutputStream());
}

public static void write(TList chars, OutputStream outputStream) throws IOException {
  TList curr = chars;
  while (curr instanceof DCons) {
    DCons cons = (DCons) curr;
      
    frege.rt.Box.Byte by = (frege.rt.Box.Byte) cons.mem1._e();
    byte b = by.j;
    outputStream.write(b);

    curr = (TList) cons.mem2._e();
  }
}

public static TResponse service(TRequest request) {
  Lam1 service1 = (Lam1) FregeWeb.service(request);
  Lazy<FV> service2 = service1.eval(Box.mk(request));
  return (TResponse) service2._e();
}

public static String uriOf(HttpServletRequest req) {
  if (req.getRequestURI().startsWith(req.getServletPath())) {
    return req.getRequestURI().substring(req.getServletPath().length());
  } else {
    return req.getRequestURI();
  }
}
```

One thing overlooked by this implementation is the character set of strings converted into a response bodies, so it would be useful to add a function for calling the native `getBytes(Charset charset)` on a string, then mapping that to the Frege `[Byte]` type in the response.
