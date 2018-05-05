package gv_fiqst.ghostfollower.domain.repo;


public interface ModelMapper<Inner, Domain> {
    Domain toDomain(Inner inner);
    Inner toInner(Domain domain);
}
